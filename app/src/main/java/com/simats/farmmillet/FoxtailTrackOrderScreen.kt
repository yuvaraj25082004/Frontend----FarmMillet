package com.simats.farmmillet

import androidx.compose.foundation.background
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoxtailTrackOrderScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Track Order", fontWeight = FontWeight.Bold) },
                navigationIcon = { IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back") } },
                actions = {
                    IconButton(onClick = { navController.navigate(AppRoutes.CONSUMER_NOTIFICATIONS) }) { Icon(Icons.Default.Notifications, "Notifications") }
                    IconButton(onClick = { navController.navigate(AppRoutes.CONSUMER_USER_PROFILE) }) { Icon(Icons.Default.Person, "Profile") }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFFF8F9FA))
            )
        },
        bottomBar = {
            OutlinedButton(
                onClick = { navController.navigate(AppRoutes.CONSUMER_DASHBOARD) { popUpTo(AppRoutes.CONSUMER_DASHBOARD) { inclusive = true } } },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text("Back to Home")
            }
        },
        containerColor = Color(0xFFF8F9FA)
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(Icons.Default.LocalShipping, "Arriving Today", modifier = Modifier.size(48.dp), tint = Color(0xFF1FA74A))
            Spacer(modifier = Modifier.height(8.dp))
            Text("Arriving Today", fontWeight = FontWeight.Bold, fontSize = 20.sp)
            Text("Order #o3", color = Color.Gray)
            Spacer(modifier = Modifier.height(24.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Timeline(isCompleted = true, title = "Order Placed", time = "10:00 AM")
                    Timeline(isCompleted = true, title = "Confirmed", time = "10:05 AM")
                    Timeline(isCompleted = true, title = "Picked Up", time = "02:00 PM")
                    Timeline(isCompleted = false, title = "In Transit", time = "Now")
                    Timeline(isCompleted = false, title = "Delivered", time = "Expected 6:00 PM", isLast = true)
                }
            }
        }
    }
}

@Composable
private fun Timeline(isCompleted: Boolean, title: String, time: String, isLast: Boolean = false) {
    Row {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(horizontal = 8.dp)) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .background(
                        if (isCompleted) Color(0xFF1FA74A) else Color.LightGray,
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (isCompleted) {
                    Icon(Icons.Default.CheckCircle, null, tint = Color.White, modifier = Modifier.size(16.dp))
                }
            }
            if (!isLast) {
                Divider(modifier = Modifier.height(48.dp).width(2.dp), color = if (isCompleted) Color(0xFF1FA74A) else Color.LightGray)
            }
        }
        Column {
            Text(title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Text(time, color = Color.Gray, fontSize = 12.sp)
        }
    }
}
