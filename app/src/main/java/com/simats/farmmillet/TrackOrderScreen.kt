package com.simats.farmmillet

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

import androidx.compose.runtime.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrackOrderScreen(navController: NavController, orderId: String? = null) {
    var trackingDetails by remember { mutableStateOf<OrderDetailsData?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(orderId) {
        if (orderId == null) {
            errorMessage = "Order ID missing"
            isLoading = false
            return@LaunchedEffect
        }
        try {
            val response = RetrofitClient.instance.trackOrder(orderId.toInt())
            if (response.isSuccessful && response.body()?.success == true) {
                trackingDetails = response.body()?.data?.order
            } else {
                errorMessage = response.body()?.message ?: "Failed to fetch tracking details"
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
                title = { Text("Logistics Status", fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { navController.navigate(AppRoutes.CONSUMER_NOTIFICATIONS) }) {
                        Icon(Icons.Default.Notifications, "Notifications")
                    }
                    IconButton(onClick = { navController.navigate(AppRoutes.CONSUMER_USER_PROFILE) }) {
                        Icon(Icons.Default.Person, "Profile")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        containerColor = Color(0xFFF3F4F9) // Slightly bluish background like image
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center), color = Color(0xFF1FA74A))
            } else if (errorMessage != null) {
                Text(errorMessage!!, modifier = Modifier.align(Alignment.Center), color = Color.Red)
            } else if (trackingDetails != null) {
                val order = trackingDetails!!
                val status = order.status.lowercase()
                
                Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(2.dp)
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text("Order ID #${order.id}", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                                    Text(
                                        "${order.items.size} Items | ₹${order.totalAmount.toInt()}",
                                        color = Color.Gray,
                                        fontSize = 14.sp
                                    )
                                }
                                
                                Surface(
                                    color = Color(0xFFE3F2FD),
                                    shape = RoundedCornerShape(16.dp)
                                ) {
                                    val displayStatus = order.status.replace("_", " ").replaceFirstChar { char -> char.uppercase() }
                                    Text(
                                        text = displayStatus,
                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                        color = Color(0xFF1976D2),
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                            
                            Spacer(modifier = Modifier.height(20.dp))
                            
                            Text("SHG: ${order.shgName ?: "Assigned SHG"}", fontWeight = FontWeight.SemiBold)
                            Text("Farmer: ${order.items.firstOrNull()?.farmerName ?: "Associated Farmer"}", fontWeight = FontWeight.SemiBold)
                            
                            Spacer(modifier = Modifier.height(16.dp))
                            
                            Text("Pickup: ${order.pickupLocation ?: "Not available"}", color = Color.Gray, fontSize = 14.sp)
                            Text("Drop-off: ${order.dropoffLocation ?: "Not available"}", color = Color.Gray, fontSize = 14.sp)
                            
                            Spacer(modifier = Modifier.height(20.dp))
                            Divider(color = Color.LightGray.copy(alpha = 0.5f))
                            Spacer(modifier = Modifier.height(20.dp))
                            
                            val stages = listOf("Order Placed", "Confirmed", "Picked Up", "In Transit", "Delivered")
                            val currentStageIndex = when (status) {
                                "order_placed" -> 0
                                "confirmed" -> 1
                                "picked_up" -> 2
                                "in_transit" -> 3
                                "delivered" -> 4
                                else -> 0
                            }
                            
                            for ((index, stage) in stages.withIndex()) {
                                TimelineItem(
                                    title = stage,
                                    isCompleted = index < currentStageIndex,
                                    isActive = index == currentStageIndex,
                                    isLast = index == stages.size - 1
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
private fun TimelineItem(title: String, isCompleted: Boolean, isActive: Boolean, isLast: Boolean) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.width(32.dp)) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .background(
                        color = when {
                            isCompleted -> Color(0xFF4CAF50)
                            isActive -> Color(0xFF1565C0)
                            else -> Color.Transparent
                        },
                        shape = CircleShape
                    )
                    .border(
                        width = 2.dp,
                        color = if (isCompleted || isActive) Color.Transparent else Color.Gray,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (isCompleted) {
                    Icon(Icons.Default.CheckCircle, null, tint = Color.White, modifier = Modifier.size(24.dp))
                } else if (isActive) {
                    Box(modifier = Modifier.size(8.dp).background(Color.White, CircleShape))
                }
            }
            if (!isLast) {
                Box(
                    modifier = Modifier
                        .width(2.dp)
                        .height(36.dp)
                        .background(if (isCompleted) Color(0xFF4CAF50) else Color.LightGray)
                )
            }
        }
        
        Spacer(modifier = Modifier.width(16.dp))
        
        Text(
            title,
            modifier = Modifier.padding(top = 2.dp),
            fontWeight = if (isActive) FontWeight.Bold else FontWeight.Normal,
            color = if (isCompleted || isActive) Color.Black else Color.Gray,
            fontSize = 16.sp
        )
    }
}
