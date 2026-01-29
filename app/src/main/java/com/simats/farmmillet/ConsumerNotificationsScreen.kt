package com.simats.farmmillet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
fun ConsumerNotificationsScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Notifications", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { navController.navigate(AppRoutes.CONSUMER_USER_PROFILE) }) {
                        Icon(Icons.Filled.Person, contentDescription = "Profile")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFFF9F9F9))
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF9F9F9))
                .padding(padding)
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Recent Alerts ( 2 )", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Row {
                    TextButton(onClick = { /*TODO*/ }) {
                        Icon(Icons.Default.Check, contentDescription = "Mark all read", modifier = Modifier.size(16.dp))
                        Text("Mark all read", fontSize = 12.sp)
                    }
                    TextButton(onClick = { /*TODO*/ }) {
                        Icon(Icons.Default.Delete, contentDescription = "Clear", modifier = Modifier.size(16.dp), tint = Color.Red)
                        Text("Clear", color = Color.Red, fontSize = 12.sp)
                    }
                }
            }
            Spacer(modifier = Modifier.padding(8.dp))
            NotificationItem(
                icon = Icons.Default.Info,
                title = "Order Delivered",
                subtitle = "Your order #o1 has been delivered successfully.",
                date = "2023-12-18",
                iconBackgroundColor = Color(0xFFE3F2FD),
                iconTint = Color(0xFF0D47A1)
            )
            Spacer(modifier = Modifier.padding(8.dp))
            NotificationItem(
                icon = Icons.Default.Info,
                title = "New Product Alert",
                subtitle = "Fresh Foxtail Millet is now available.",
                date = "2023-12-10",
                iconBackgroundColor = Color(0xFFE3F2FD),
                iconTint = Color(0xFF0D47A1)
            )
        }
    }
}

@Composable
fun NotificationItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    date: String,
    iconBackgroundColor: Color,
    iconTint: Color
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(iconBackgroundColor, CircleShape)
                    .padding(12.dp)
            ) {
                Icon(icon, contentDescription = null, tint = iconTint)
            }
            Spacer(modifier = Modifier.padding(8.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(subtitle, fontSize = 14.sp, color = Color.Gray)
            }
            Text(date, fontSize = 12.sp, color = Color.Gray)
        }
    }
}
