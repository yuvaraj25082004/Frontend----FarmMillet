package com.simats.farmmillet

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyOrdersScreen(navController: NavController) {
    var orders by remember { mutableStateOf<List<OrderResponse>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        try {
            val response = RetrofitClient.instance.getMyOrders()
            if (response.isSuccessful && response.body()?.success == true) {
                orders = response.body()?.data ?: emptyList()
            } else {
                errorMessage = response.body()?.message ?: "Failed to fetch your orders"
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
                title = { Text("My Orders", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { navController.navigate(AppRoutes.CONSUMER_NOTIFICATIONS) }) {
                        Icon(imageVector = Icons.Default.Notifications, contentDescription = "Notifications")
                    }
                }
            )
        },
        bottomBar = { ConsumerBottomNavigationBar(navController) }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues).fillMaxSize()) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center), color = Color(0xFF1FA74A))
            } else if (errorMessage != null) {
                Text(errorMessage!!, modifier = Modifier.align(Alignment.Center), color = Color.Red)
            } else if (orders.isEmpty()) {
                Column(modifier = Modifier.align(Alignment.Center), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("No orders yet", color = Color.Gray, fontSize = 18.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = { navController.navigate(AppRoutes.CONSUMER_MARKETPLACE) }, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1FA74A))) {
                        Text("Start Shopping")
                    }
                }
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                    items(orders.size) { index ->
                        ConsumerOrderCard(
                            order = orders[index],
                            onClick = { navController.navigate("track_order/${orders[index].id}") }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ConsumerOrderCard(order: OrderResponse, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Order #${order.id}", fontWeight = FontWeight.Bold, color = Color.Gray)
                    Spacer(modifier = Modifier.width(8.dp))
                    StatusBadge(order.status)
                }
                Spacer(modifier = Modifier.height(8.dp))
                // Try to get millet type from the first item if available, otherwise generic
                val milletType = order.items?.firstOrNull()?.productName ?: "Millet Products"
                Text(milletType, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text("${order.items?.size ?: 0} Items • ₹${order.totalAmount.toInt()}", color = Color.Gray)
                Text("Date: ${order.createdAt.split("T")[0]}", fontSize = 12.sp, color = Color.LightGray)
            }
            Icon(imageVector = Icons.Default.ArrowForwardIos, contentDescription = null, modifier = Modifier.size(16.dp), tint = Color.Gray)
        }
    }
}

@Composable
fun StatusBadge(status: String) {
    val (backgroundColor, textColor) = when (status.lowercase()) {
        "order_placed" -> Color(0xFFE3F2FD) to Color(0xFF1976D2)
        "confirmed" -> Color(0xFFFFF3E0) to Color(0xFFE65100)
        "shipped", "in_transit" -> Color(0xFFE1BEE7) to Color(0xFF8E24AA)
        "delivered" -> Color(0xFFE8F5E9) to Color(0xFF2E7D32)
        else -> Color(0xFFF5F5F5) to Color(0xFF757575)
    }
    
    Surface(
        color = backgroundColor,
        shape = RoundedCornerShape(4.dp)
    ) {
        val displayStatus = status.replace("_", " ").replaceFirstChar { char -> char.uppercase() }
        Text(
            text = displayStatus,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            color = textColor,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
