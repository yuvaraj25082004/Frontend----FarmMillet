package com.simats.farmmillet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

import androidx.compose.runtime.*
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MySupplyListScreen(navController: NavController) {
    val context = LocalContext.current
    var supplies by remember { mutableStateOf<List<SupplyResponse>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        try {
            val response = RetrofitClient.instance.getMySupplies()
            if (response.isSuccessful && response.body()?.success == true) {
                supplies = response.body()?.data ?: emptyList()
            } else {
                errorMessage = response.body()?.message ?: "Failed to fetch supplies"
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
                title = { Text("My Supply List", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { navController.navigate(AppRoutes.NOTIFICATIONS) }) {
                        Icon(Icons.Default.Notifications, contentDescription = "Notifications")
                    }
                    IconButton(onClick = { navController.navigate(AppRoutes.USER_PROFILE) }) {
                        Icon(Icons.Default.Person, contentDescription = "Profile")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(AppRoutes.ADD_MILLET_SUPPLY) },
                containerColor = Color(0xFF1FA74A)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add New", tint = Color.White)
            }
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center), color = Color(0xFF1FA74A))
            } else if (errorMessage != null) {
                Text(errorMessage!!, modifier = Modifier.align(Alignment.Center), color = Color.Red)
            } else if (supplies.isEmpty()) {
                Text("No supplies found", modifier = Modifier.align(Alignment.Center), color = Color.Gray)
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    items(supplies) { supply ->
                        SupplyListItem(navController, supply)
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun SupplyListItem(navController: NavController, supply: SupplyResponse) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(supply.milletType, fontWeight = FontWeight.Bold, fontSize = 18.sp, modifier = Modifier.weight(1f))
                StatusChip(supply.status)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text("Quantity: ${supply.quantityKg} kg")
            Text("Harvest Date: ${supply.createdAt.split("T")[0]}") // Using created_at as harvest date if needed or just display it
            Text("Grade: ${supply.qualityGrade}")

            Spacer(modifier = Modifier.height(16.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End, verticalAlignment = Alignment.CenterVertically) {
                TextButton(onClick = { navController.navigate("logistics_status/${supply.id}") }) {
                    Text("Track", color = Color(0xFF1FA74A))
                }
                IconButton(onClick = { /* Handle Edit */ }) {
                    Icon(Icons.Default.Edit, contentDescription = "Edit", tint = Color.Gray)
                }
                IconButton(onClick = { /* Handle Delete */ }) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Red)
                }
            }
        }
    }
}

@Composable
fun StatusChip(status: String) {
    val (backgroundColor, textColor) = when (status) {
        "Collected" -> Color(0xFFC8E6C9) to Color(0xFF388E3C)
        "Accepted" -> Color(0xFFFFF9C4) to Color(0xFFFBC02D)
        "Listed" -> Color(0xFFBBDEFB) to Color(0xFF1976D2)
        else -> Color.Gray to Color.White
    }

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(backgroundColor)
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(status, color = textColor, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
    }
}

data class SupplyItemData(
    val milletType: String,
    val quantity: String,
    val harvestDate: String,
    val grade: String,
    val collectedBy: String,
    val collectionDate: String,
    val status: String
)
