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
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
fun LittleMilletTraceabilityScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Traceability Journey", fontWeight = FontWeight.Bold) },
                navigationIcon = { IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back") } },
                actions = {
                    IconButton(onClick = { navController.navigate(AppRoutes.CONSUMER_NOTIFICATIONS) }) { Icon(Icons.Default.Notifications, "Notifications") }
                    IconButton(onClick = { navController.navigate(AppRoutes.CONSUMER_USER_PROFILE) }) { Icon(Icons.Default.Person, "Profile") }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFFF8F9FA))
            )
        },
        containerColor = Color(0xFFF8F9FA)
    ) { padding ->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .padding(16.dp)) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)),
            ) {
                Column(
                    modifier = Modifier.padding(24.dp).fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Traceability ID", color = Color(0xFF1B5E20))
                    Text("TR-2023-001", fontWeight = FontWeight.Bold, fontSize = 24.sp, color = Color(0xFF1B5E20))
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            Timeline()
        }
    }
}

@Composable
private fun Timeline() {
    Column {
        TimelineItem("Harvested", "2023-11-15", "Harvested by Ramesh Kumar in Mandya", Icons.Default.Eco, Color(0xFFE8F5E9), Color(0xFF1B5E20))
        TimelineItem("Processed & Packed", "2023-12-01", "Quality checked (Grade A) and packed by Mahila Shakti SHG", Icons.Default.VerifiedUser, Color(0xFFE3F2FD), Color(0xFF0D47A1))
        TimelineItem("Verified", "2023-12-01", "Quality assurance completed and verified", Icons.Default.CheckCircle, Color(0xFFF3E5F5), Color(0xFF673AB7), isLast = true)
    }
}

@Composable
private fun TimelineItem(title: String, date: String, description: String, icon: ImageVector, iconBackgroundColor: Color, iconTint: Color, isLast: Boolean = false) {
    Row {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(horizontal = 8.dp)) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(iconBackgroundColor, CircleShape)
                    .padding(12.dp)
            ) {
                Icon(icon, contentDescription = null, tint = iconTint)
            }
            if (!isLast) {
                Divider(modifier = Modifier.height(64.dp).width(2.dp), color = Color.LightGray)
            }
        }
        Column(modifier = Modifier.padding(start = 8.dp)) {
            Text(title, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Text(date, color = Color.Gray, fontSize = 12.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(description, color = Color.Gray, fontSize = 14.sp)
        }
    }
}
