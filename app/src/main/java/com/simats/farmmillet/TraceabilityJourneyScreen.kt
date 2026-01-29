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

import androidx.compose.runtime.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TraceabilityJourneyScreen(navController: NavController, traceabilityId: String?) {
    var journeyData by remember { mutableStateOf<TraceabilityResponse?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(traceabilityId) {
        if (traceabilityId == null) {
            errorMessage = "No Traceability ID provided"
            isLoading = false
            return@LaunchedEffect
        }
        try {
            val response = RetrofitClient.instance.getTraceabilityDetails(traceabilityId)
            if (response.isSuccessful && response.body()?.success == true) {
                journeyData = response.body()?.data
            } else {
                errorMessage = response.body()?.message ?: "Failed to fetch journey"
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
                    Text(traceabilityId ?: "N/A", fontWeight = FontWeight.Bold, fontSize = 24.sp, color = Color(0xFF1B5E20))
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            
            Box(modifier = Modifier.fillMaxSize()) {
                if (isLoading) {
                    androidx.compose.material3.CircularProgressIndicator(modifier = Modifier.align(Alignment.Center), color = Color(0xFF1FA74A))
                } else if (errorMessage != null) {
                    Text(errorMessage!!, modifier = Modifier.align(Alignment.Center), color = Color.Red)
                } else if (journeyData != null) {
                    val displayJourney = if (journeyData!!.journey.isNotEmpty()) {
                        journeyData!!.journey
                    } else {
                        // Construct journey from traceability record
                        val record = journeyData!!.traceability
                        val points = mutableListOf<JourneyPoint>()
                        
                        // 1. Harvested
                        points.add(JourneyPoint(
                            stage = "Harvested",
                            location = record.farmLocation ?: "Farmer's Field",
                            date = record.harvestDate,
                            actor = record.farmerName
                        ))
                        
                        // 2. Packaged
                        points.add(JourneyPoint(
                            stage = "Packaged",
                            location = record.farmLocation ?: "Farmer's Premises",
                            date = record.packagingDate,
                            actor = record.farmerName
                        ))
                        
                        // 3. Collected (if status is accepted or beyond)
                        if (record.supplyStatus != "pending") {
                            points.add(JourneyPoint(
                                stage = "Collected",
                                location = record.location ?: "Collection Center",
                                date = record.collectionDate ?: record.createdAt,
                                actor = record.collectionBy ?: "SHG Partner"
                            ))
                        }
                        
                        // 4. Verified/Listed
                        points.add(JourneyPoint(
                            stage = "Verified",
                            location = "SHG Processing Unit",
                            date = record.createdAt,
                            actor = "Quality Assurance Team"
                        ))
                        
                        points
                    }
                    Timeline(displayJourney)
                } else {
                    Text("No journey data found", modifier = Modifier.align(Alignment.Center))
                }
            }
        }
    }
}

@Composable
private fun Timeline(journey: List<JourneyPoint>) {
    Column {
        for ((index, point) in journey.withIndex()) {
            val icon = when (point.stage.lowercase()) {
                "harvested" -> Icons.Default.Eco
                "verified" -> Icons.Default.CheckCircle
                else -> Icons.Default.VerifiedUser
            }
            TimelineItem(
                title = point.stage,
                date = point.date,
                description = "${point.actor} at ${point.location}",
                icon = icon,
                iconBackgroundColor = if (index == 0) Color(0xFFE8F5E9) else Color(0xFFE3F2FD),
                iconTint = if (index == 0) Color(0xFF1B5E20) else Color(0xFF0D47A1),
                isLast = index == journey.size - 1
            )
        }
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
