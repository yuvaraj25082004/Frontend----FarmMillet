package com.simats.farmmillet

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.draw.clip
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogisticsStatusScreen(navController: NavController, supplyId: String? = null) {
    val context = LocalContext.current
    var supplyData by remember { mutableStateOf<SupplyResponse?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(supplyId) {
        if (supplyId == null) {
            errorMessage = "Invalid Supply ID"
            isLoading = false
            return@LaunchedEffect
        }
        try {
            val response = RetrofitClient.instance.getSupplyById(supplyId.toInt())
            if (response.isSuccessful && response.body()?.success == true) {
                supplyData = response.body()?.data
            } else {
                errorMessage = response.body()?.message ?: "Failed to fetch supply tracking"
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
                title = { Text("Logistics Status") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { navController.navigate(AppRoutes.NOTIFICATIONS) }) { Icon(Icons.Default.Notifications, "Notifications") }
                    IconButton(onClick = { navController.navigate(AppRoutes.USER_PROFILE) }) { Icon(Icons.Default.Person, "Profile") }
                }
            )
        },
        bottomBar = { BottomNavigationBar(navController) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally), color = Color(0xFF1FA74A))
            } else if (errorMessage != null) {
                Text(errorMessage!!, color = Color.Red, modifier = Modifier.align(Alignment.CenterHorizontally))
            } else if (supplyData != null) {
                val supply = supplyData!!
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(4.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("Supply ID #${supply.id}", fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                            StatusChip(
                                text = supply.status.replaceFirstChar { it.uppercase() },
                                backgroundColor = if (supply.status == "pending") Color(0xFFFFF3E0) else Color(0xFFE8F5E9),
                                textColor = if (supply.status == "pending") Color(0xFFE65100) else Color(0xFF2E7D32)
                            )
                        }
                        Text("${supply.milletType} | ${supply.quantityKg} kg", color = Color.Gray)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("SHG: ${supply.shgName ?: "To be assigned"}", fontWeight = FontWeight.SemiBold)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Collected By: ${supply.collectionBy ?: "Pending Assignment"}", color = Color.Gray)
                        Text("Collection Date: ${supply.collectionDate ?: "TBA"}", color = Color.Gray)
                        Spacer(modifier = Modifier.height(16.dp))
                        Divider()
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        val stages = listOf("Supply Added", "SHG Accepted", "Logistics Assigned", "Collected", "Completed")
                        val currentStage = when (supply.status) {
                            "pending" -> 0
                            "accepted" -> if (supply.collectionBy != null) 2 else 1
                            "collected" -> 3
                            "completed" -> 4
                            else -> 0
                        }
                        Timeline(stages, currentStage)
                    }
                }
            }
        }
    }
}

@Composable
fun Timeline(stages: List<String>, currentStageIndex: Int) {
    LazyColumn {
        items(stages.size) {
            TimelineNode(stage = stages[it], isCompleted = it < currentStageIndex, isCurrent = it == currentStageIndex, isLast = it == stages.size - 1)
        }
    }
}

@Composable
fun TimelineNode(stage: String, isCompleted: Boolean, isCurrent: Boolean, isLast: Boolean) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .background(color = if (isCompleted) Color(0xFF1FA74A) else Color.LightGray, shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                if (isCompleted) {
                    Icon(Icons.Default.CheckCircle, "Completed", tint = Color.White)
                } else if (isCurrent) {
                    Icon(Icons.Default.Circle, "Current", tint = Color(0xFF0D47A1), modifier = Modifier.size(16.dp))
                } else {
                    Icon(Icons.Default.RadioButtonUnchecked, "Pending", tint = Color.Gray)
                }
            }
            if (!isLast) {
                Divider(modifier = Modifier.height(40.dp).width(2.dp), color = if(isCompleted) Color(0xFF1FA74A) else Color.LightGray)
            }
        }
        Spacer(modifier = Modifier.width(16.dp))
        Text(stage, fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal)
    }
}


