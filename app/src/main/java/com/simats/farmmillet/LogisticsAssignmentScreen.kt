package com.simats.farmmillet

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.ExpandMore
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogisticsAssignmentScreen(navController: NavController) {
    var pickupLocation by remember { mutableStateOf("Mahila Shakti SHG Center, Mysuru") }
    var deliveryLocation by remember { mutableStateOf("789 Urban Apts, Bengaluru") }
    var logisticsPartner by remember { mutableStateOf("India Post") }
    var pickupDate by remember { mutableStateOf("2025-10-25") }
    var vehicleType by remember { mutableStateOf("truck") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Logistics Assignment", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { navController.navigate(AppRoutes.NOTIFICATIONS) }) {
                        Icon(Icons.Default.Notifications, contentDescription = "Notifications")
                    }
                    IconButton(onClick = { navController.navigate(AppRoutes.SHG_USER_PROFILE) }) {
                        Icon(Icons.Default.Person, contentDescription = "Profile")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        containerColor = Color(0xFFF8F9FA)
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Schedule Pickup", fontWeight = FontWeight.SemiBold, fontSize = 18.sp)
                    Spacer(modifier = Modifier.height(16.dp))
                    InfoTextField(label = "Pickup Location", value = pickupLocation, onValueChange = { pickupLocation = it })
                    Spacer(modifier = Modifier.height(16.dp))
                    InfoTextField(label = "Delivery Location", value = deliveryLocation, onValueChange = { deliveryLocation = it })
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(value = logisticsPartner, onValueChange = {}, label = { Text("Logistics Partner") }, modifier = Modifier.fillMaxWidth(), readOnly = true, trailingIcon = { Icon(Icons.Default.ExpandMore, null) })
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(value = pickupDate, onValueChange = { pickupDate = it }, label = { Text("Pickup Date") }, modifier = Modifier.fillMaxWidth(), trailingIcon = { Icon(Icons.Default.CalendarToday, null) })
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(value = vehicleType, onValueChange = { vehicleType = it }, label = { Text("Vehicle Type") }, modifier = Modifier.fillMaxWidth())
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = { /* TODO: Confirm & Schedule */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1FA74A)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Confirm & Schedule", fontSize = 16.sp)
            }
        }
    }
}

@Composable
private fun InfoTextField(label: String, value: String, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth(),
        readOnly = true,
        colors = OutlinedTextFieldDefaults.colors(
            disabledTextColor = LocalContentColor.current,
            disabledBorderColor = MaterialTheme.colorScheme.outline,
            disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
    )
}
