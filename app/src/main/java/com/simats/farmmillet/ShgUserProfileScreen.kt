package com.simats.farmmillet

import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShgUserProfileScreen(navController: NavController) {
    val context = LocalContext.current
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("SHG Profile", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { navController.navigate(AppRoutes.NOTIFICATIONS) }) {
                        Icon(Icons.Default.Notifications, contentDescription = "Notifications")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                Surface(
                    modifier = Modifier.size(100.dp).clip(CircleShape),
                    color = Color(0xFFE3F2FD)
                ) {
                   Icon(imageVector = Icons.Default.Person, contentDescription = "User Avatar", tint = Color(0xFF0D47A1), modifier = Modifier.padding(20.dp))
                }
                Spacer(modifier = Modifier.height(16.dp))
                val context = LocalContext.current
                Text(TokenManager.getUserName(context).ifEmpty { "SHG User" }, fontWeight = FontWeight.Bold, fontSize = 22.sp)
                Text("SHG / FPO Member", color = Color.Gray, fontSize = 16.sp)
                Text(TokenManager.getUserCity(context), color = Color.Gray, fontSize = 14.sp)
            }
            Spacer(modifier = Modifier.height(24.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        val context = LocalContext.current
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("Organization Details", fontWeight = FontWeight.Bold, fontSize = 18.sp, modifier = Modifier.weight(1f))
                            Icon(Icons.Default.Edit, contentDescription = "Edit", tint = Color.Gray, modifier = Modifier.clickable { navController.navigate(AppRoutes.SHG_FPO_PROFILE) })
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Divider()
                        Spacer(modifier = Modifier.height(16.dp))
                        InfoRow(icon = Icons.Default.Person, label = "Organization Name", value = TokenManager.getUserName(context))
                        InfoRow(icon = Icons.Default.Email, label = "Email", value = TokenManager.getUserEmail(context))
                        InfoRow(icon = Icons.Default.Phone, label = "Mobile", value = TokenManager.getUserMobile(context))
                        InfoRow(icon = Icons.Default.LocationOn, label = "Address", value = "${TokenManager.getUserStreet(context)}, ${TokenManager.getUserCity(context)}")
                    }
            }
            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = { 
                    TokenManager.clearToken(context)
                    RetrofitClient.setToken(null)
                    navController.navigate(AppRoutes.WELCOME) { 
                        popUpTo(0) { inclusive = true } 
                    } 
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F))
            ) {
                Text("Logout", color = Color.White, fontSize = 16.sp)
            }
        }
    }
}

@Composable
private fun InfoRow(icon: ImageVector, label:String, value: String) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 8.dp)) {
        Icon(icon, contentDescription = label, tint = Color.Gray)
        Spacer(modifier = Modifier.width(16.dp))
        Text(value, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
    }
}
