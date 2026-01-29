package com.simats.farmmillet

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Search
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
fun TraceabilityRecordsScreen(navController: NavController) {
    var searchQuery by remember { mutableStateOf("") }
    var records by remember { mutableStateOf<List<TraceabilityRecordResponse>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        try {
            val response = RetrofitClient.instance.getAllTraceability()
            if (response.isSuccessful && response.body()?.success == true) {
                records = response.body()?.data?.records ?: emptyList()
            } else {
                errorMessage = response.body()?.message ?: "Failed to fetch records"
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
                title = { Text("Traceability Records", fontWeight = FontWeight.Bold) },
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
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search by Traceability ID") },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                shape = RoundedCornerShape(12.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))

            if (isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Color(0xFF1FA74A))
                }
            } else if (errorMessage != null) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(errorMessage!!, color = Color.Red)
                }
            } else {
                val filteredRecords = if (searchQuery.isEmpty()) {
                    records
                } else {
                    records.filter { it.traceabilityId.contains(searchQuery, ignoreCase = true) }
                }

                if (filteredRecords.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("No traceability records found", color = Color.Gray)
                    }
                } else {
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        items(filteredRecords) { record ->
                            TraceabilityRecordItem(record)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TraceabilityRecordItem(record: TraceabilityRecordResponse) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.Top) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("ID: ${record.traceabilityId}", color = Color.Gray, fontSize = 12.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(record.milletType, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                }
                Icon(Icons.Default.QrCodeScanner, contentDescription = "QR Code", modifier = Modifier.size(48.dp))
            }
            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(16.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.weight(1f)) {
                    TraceabilityDetailRow("Farmer", record.farmerName)
                    TraceabilityDetailRow("Harvest Date", record.harvestDate)
                }
                Column(modifier = Modifier.weight(1f)) {
                    TraceabilityDetailRow("Packaging Date", record.packagingDate)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Grade", color = Color.Gray, fontSize = 14.sp, modifier = Modifier.width(100.dp))
                        Surface(shape = RoundedCornerShape(4.dp), color = Color(0xFFE8F5E9)){
                            Text(record.qualityGrade, color = Color(0xFF1FA74A), modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp), fontSize = 12.sp, fontWeight = FontWeight.Medium)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TraceabilityDetailRow(label: String, value: String) {
    Row(modifier = Modifier.padding(bottom = 8.dp)) {
        Text(label, color = Color.Gray, fontSize = 14.sp, modifier = Modifier.width(100.dp))
        Text(value, fontWeight = FontWeight.Medium, fontSize = 14.sp)
    }
}
