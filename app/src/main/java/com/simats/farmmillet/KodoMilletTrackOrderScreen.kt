package com.simats.farmmillet

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KodoMilletTrackOrderScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Track Order") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { navController.navigate(AppRoutes.NOTIFICATIONS) }) {
                        Icon(Icons.Default.Notifications, contentDescription = "Notifications")
                    }
                    IconButton(onClick = { navController.navigate(AppRoutes.USER_PROFILE) }) {
                        Icon(Icons.Default.Person, contentDescription = "Profile")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.LocalShipping,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(64.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text("Arriving Today", fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Text("Order # o1", fontSize = 16.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(32.dp))
            Card(shape = RoundedCornerShape(16.dp), modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    TrackOrderItem(title = "Order Placed", time = "10:00 AM", isCompleted = true)
                    TrackOrderItem(title = "Confirmed", time = "10:05 AM", isCompleted = true)
                    TrackOrderItem(title = "Picked Up", time = "02:00 PM", isCompleted = true)
                    TrackOrderItem(title = "In Transit", time = "Now", isCompleted = false, isCurrent = true)
                    TrackOrderItem(title = "Delivered", time = "Expected 6:00 PM", isCompleted = false, isLast = true)
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = { navController.navigate(AppRoutes.CONSUMER_DASHBOARD) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = MaterialTheme.colorScheme.primary)
            ) {
                Text("Back to Home")
            }
        }
    }
}

@Composable
fun TrackOrderItem(
    title: String,
    time: String,
    isCompleted: Boolean,
    isCurrent: Boolean = false,
    isLast: Boolean = false
) {
    Row {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(24.dp)
            ) {
                if (isCompleted) {
                    Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF4CAF50))
                } else if (isCurrent) {
                    Icon(imageVector = Icons.Default.RadioButtonChecked, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                } else {
                    Icon(imageVector = Icons.Default.RadioButtonUnchecked, contentDescription = null, tint = Color.Gray)
                }
            }
            if (!isLast) {
                Divider(modifier = Modifier.height(40.dp).width(1.dp))
            }
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(title, fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal)
            Text(time, color = Color.Gray, fontSize = 12.sp)
        }
    }
}
