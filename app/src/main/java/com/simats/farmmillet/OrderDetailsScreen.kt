package com.simats.farmmillet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import android.widget.Toast

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderDetailsScreen(navController: NavController, orderId: String?) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var orderData by remember { mutableStateOf<OrderResponse?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(orderId) {
        if (orderId == null) {
            errorMessage = "Invalid Order ID"
            isLoading = false
            return@LaunchedEffect
        }
        try {
            // SHG fetches all orders for now and we find the one we need
            // In a real app, we'd have getOrderDetails endpoint
            val response = RetrofitClient.instance.getShgOrdersReceived()
            if (response.isSuccessful && response.body()?.success == true) {
                orderData = response.body()?.data?.find { it.id.toString() == orderId }
                if (orderData == null) errorMessage = "Order not found"
            } else {
                errorMessage = response.body()?.message ?: "Failed to fetch order details"
            }
        } catch (e: Exception) {
            errorMessage = "Error: ${e.message}"
        } finally {
            isLoading = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Order Details", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { navController.navigate(AppRoutes.NOTIFICATIONS) }) {
                        Icon(Icons.Default.Notifications, contentDescription = "Notifications")
                    }
                }
            )
        },
        containerColor = Color(0xFFF8F9FA)
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center), color = Color(0xFF1FA74A))
            } else if (errorMessage != null) {
                Text(errorMessage!!, modifier = Modifier.align(Alignment.Center), color = Color.Red)
            } else if (orderData != null) {
                val order = orderData!!
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    OrderInfoCard(order)
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    if (order.status.lowercase() != "delivered" && order.status.lowercase() != "cancelled") {
                        val nextStatus = when (order.status.lowercase()) {
                            "order_placed" -> "confirmed"
                            "confirmed" -> "picked_up"
                            "picked_up" -> "in_transit"
                            "in_transit" -> "delivered"
                            else -> "delivered"
                        }
                        
                        Button(
                            onClick = {
                                isLoading = true
                                scope.launch {
                                    try {
                                        val res = RetrofitClient.instance.updateOrderStatus(order.id, mapOf("status" to nextStatus))
                                        if (res.isSuccessful) {
                                            Toast.makeText(context, "Status Updated to ${nextStatus}!", Toast.LENGTH_SHORT).show()
                                            // Refresh
                                            val refreshRes = RetrofitClient.instance.getShgOrdersReceived()
                                            if (refreshRes.isSuccessful) orderData = refreshRes.body()?.data?.find { it.id == order.id }
                                        }
                                    } catch (e: Exception) {
                                        Toast.makeText(context, "Update Failed: ${e.message}", Toast.LENGTH_SHORT).show()
                                    } finally {
                                        isLoading = false
                                    }
                                }
                            },
                            modifier = Modifier.fillMaxWidth().height(50.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1FA74A)),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Update Status to ${nextStatus.replace("_", " ").uppercase()}", fontSize = 16.sp)
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    if (order.status.lowercase() == "delivered") {
                        OrderDeliveredCard()
                    } else if (order.status.lowercase() == "in_transit") {
                        OrderInTransitCard()
                    }
                }
            }
        }
    }
}

@Composable
private fun OrderInfoCard(order: OrderResponse) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Order ID: #${order.id}", fontWeight = FontWeight.Bold, fontSize = 18.sp, modifier = Modifier.weight(1f))
                StatusChip(
                    text = order.status.replace("_", " ").uppercase(),
                    backgroundColor = when(order.status.lowercase()) {
                        "delivered" -> Color(0xFFE8F5E9)
                        "cancelled" -> Color(0xFFFFEBEE)
                        else -> Color(0xFFE3F2FD)
                    },
                    textColor = when(order.status.lowercase()) {
                        "delivered" -> Color(0xFF1FA74A)
                        "cancelled" -> Color(0xFFD32F2F)
                        else -> Color(0xFF0D47A1)
                    }
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text("Consumer Details", color = Color.Gray, fontWeight = FontWeight.SemiBold)
            Text(order.consumerName ?: "Unknown Name", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Text(order.consumerAddress ?: "No address provided", color = Color.DarkGray)
            Text(order.consumerMobile ?: "No mobile provided", color = Color.DarkGray)
            
            Divider(modifier = Modifier.padding(vertical = 16.dp))
            
            Text("Items", color = Color.Gray, fontWeight = FontWeight.SemiBold)
            order.items?.forEach { item ->
                Row(modifier = Modifier.padding(vertical = 4.dp)) {
                    Text(item.productName ?: "Item", modifier = Modifier.weight(1f))
                    Text("${item.quantity ?: 0} kg x ₹${item.price ?: 0}")
                }
            }
            
            Divider(modifier = Modifier.padding(vertical = 16.dp))
            
            Row {
                Text("Total Amount", modifier = Modifier.weight(1f), fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Text("₹${order.totalAmount.toInt()}", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}


@Composable
fun OrderDeliveredCard(){
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)),
    ) {
        Row(
            modifier = Modifier.padding(24.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF1FA74A), modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.width(12.dp))
            Text("Order Delivered Successfully", fontWeight = FontWeight.SemiBold, fontSize = 16.sp, color = Color(0xFF1FA74A))
        }
    }
}

@Composable
fun OrderInTransitCard(){
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD)),
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(Icons.Default.LocalShipping, contentDescription = null, tint = Color(0xFF0D47A1), modifier = Modifier.size(32.dp))
            Spacer(modifier = Modifier.height(12.dp))
            Text("Order is on the way", fontWeight = FontWeight.SemiBold, fontSize = 16.sp, color = Color(0xFF0D47A1))
        }
    }
}
