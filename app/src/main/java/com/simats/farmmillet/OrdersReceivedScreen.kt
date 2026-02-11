package com.simats.farmmillet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrdersReceivedScreen(navController: NavController) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var selectedTabIndex by remember { mutableStateOf(0) }
    val tabs = listOf("order_placed", "confirmed", "in_transit", "delivered")
    val displayTabs = listOf("New", "Confirmed", "In Transit", "Delivered")
    
    var orders by remember { mutableStateOf<List<OrderResponse>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        try {
            val response = RetrofitClient.instance.getShgOrdersReceived()
            if (response.isSuccessful && response.body()?.success == true) {
                orders = response.body()?.data ?: emptyList()
            } else {
                errorMessage = response.body()?.message ?: "Failed to fetch orders"
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
                title = { Text("Orders Received", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { navController.navigate(AppRoutes.NOTIFICATIONS) }) {
                        Icon(imageVector = Icons.Default.Notifications, contentDescription = "Notifications")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            TabRow(selectedTabIndex = selectedTabIndex, containerColor = Color.White, contentColor = Color(0xFF1FA74A)) {
                displayTabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = { Text(title) }
                    )
                }
            }
            
            Box(modifier = Modifier.fillMaxSize()) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center), color = Color(0xFF1FA74A))
                } else if (errorMessage != null) {
                    Text(errorMessage!!, modifier = Modifier.align(Alignment.Center), color = Color.Red)
                } else {
                    val filteredOrders = orders.filter { it.status.lowercase() == tabs[selectedTabIndex].lowercase() }
                    if (filteredOrders.isEmpty()) {
                        Text("No orders in this category", modifier = Modifier.align(Alignment.Center), color = Color.Gray)
                    } else {
                        LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                            items(filteredOrders.size) { index ->
                                val order = filteredOrders[index]
                                OrderCard(
                                    orderId = "#${order.id}",
                                    milletType = order.items?.firstOrNull()?.productName ?: "Mixed Millets1",
                                    quantity = "${order.items?.firstOrNull()?.quantity ?: 0} kg",
                                    price = "₹ ${order.totalAmount.toInt()}",
                                    consumer = order.consumerName ?: "Customer",
                                    status = order.status,
                                    onClick = { 
                                        navController.navigate("order_details/${order.id}")
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun OrderCard(
    orderId: String,
    milletType: String,
    quantity: String,
    price: String,
    consumer: String,
    status: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        onClick = onClick
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Row {
                    Text(orderId, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = status,
                        color = if (status == "Delivered") Color(0xFF4CAF50) else Color(0xFF9C27B0),
                        fontSize = 12.sp,
                        modifier = Modifier
                            .background(
                                color = if (status == "Delivered") Color(0xFFC8E6C9) else Color(0xFFE1BEE7),
                                shape = RoundedCornerShape(4.dp)
                            )
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(milletType, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text("$quantity • $price")
                Text("Consumer: $consumer")
            }
            Icon(imageVector = Icons.Default.ArrowForwardIos, contentDescription = null)
        }
    }
}
