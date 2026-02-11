package com.simats.farmmillet

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

import androidx.compose.runtime.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SalesSummaryScreen(navController: NavController) {
    var salesSummary by remember { mutableStateOf<FarmerSalesSummaryResponse?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        try {
            val response = RetrofitClient.instance.getFarmerSalesSummary()
            if (response.isSuccessful && response.body()?.success == true) {
                salesSummary = response.body()?.data
            } else {
                errorMessage = response.body()?.message ?: "Failed to fetch summary"
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
                title = { Text("Sales Summary", fontWeight = FontWeight.Bold) },
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
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF9F9F9))
                .padding(padding)
                .padding(16.dp)
        ) {
            if (isLoading) {
                Box(modifier = Modifier.fillMaxSize()) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center), color = Color(0xFF1FA74A))
                }
            } else if (errorMessage != null) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Text(errorMessage!!, modifier = Modifier.align(Alignment.Center), color = Color.Red)
                }
            } else if (salesSummary != null) {
                SummaryMetrics(salesSummary!!.summary)
                Spacer(modifier = Modifier.height(24.dp))
                SalesByMilletType(salesSummary!!.milletBreakdown)
                Spacer(modifier = Modifier.height(24.dp))
                MonthlySalesTrend()
            }
        }
    }
}

@Composable
fun SummaryMetrics(stats: FarmerStats) {
    val totalSalesVal = stats.totalQuantity
    val totalRevenueVal = stats.totalEarnings
    val avgPriceVal = if (totalSalesVal > 0) totalRevenueVal / totalSalesVal else 0.0
    val totalOrdersVal = stats.totalSupplies // Or totalPayments if that represents orders clearer

    Column {
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            SummaryMetricCard("Total Sales", "${totalSalesVal.toInt()} kg", Modifier.weight(1f))
            SummaryMetricCard("Revenue", "₹${totalRevenueVal.toInt()}", Modifier.weight(1f))
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            SummaryMetricCard("Avg. Price", "₹${"%.1f".format(avgPriceVal)}/kg", Modifier.weight(1f))
            SummaryMetricCard("Supplies", "${totalOrdersVal}", Modifier.weight(1f))
        }
    }
}

@Composable
fun SummaryMetricCard(label: String, value: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(label, fontSize = 14.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(8.dp))
            Text(value, fontWeight = FontWeight.Bold, fontSize = 22.sp, color = Color(0xFF0D47A1))
        }
    }
}

@Composable
fun SalesByMilletType(Millets1ales: List<MilletBreakdown>) {
    var expandedItem by remember { mutableStateOf<String?>(null) }

    Column {
        Text("Sales by Millet Type", fontWeight = FontWeight.Bold, fontSize = 18.sp)
        Spacer(modifier = Modifier.height(16.dp))
        if (Millets1ales.isEmpty()) {
            Text("No millet-specific data available", color = Color.Gray, fontSize = 14.sp)
        } else {
            Millets1ales.forEach { breakdown ->
                Millets1alesCard(breakdown.milletType, "${breakdown.totalQuantity.toInt()} kg sold", expandedItem == breakdown.milletType) { 
                    expandedItem = if (expandedItem == breakdown.milletType) null else breakdown.milletType 
                }
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
fun Millets1alesCard(title: String, subtitle: String, expanded: Boolean, hasDetails: Boolean = false, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(title, fontWeight = FontWeight.SemiBold)
                    Text(subtitle, color = Color.Gray, fontSize = 14.sp)
                }
                Icon(if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore, contentDescription = "Expand")
            }
            if (hasDetails) {
                AnimatedVisibility(visible = expanded) {
                    Column {
                        Divider(modifier = Modifier.padding(vertical = 12.dp))
                        Row {
                            Millets1aleDetail(label = "Quantity", value = "500 kg", modifier = Modifier.weight(1f))
                            Millets1aleDetail(label = "Orders", value = "1", modifier = Modifier.weight(1f))
                            Millets1aleDetail(label = "Revenue", value = "₹225", modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Millets1aleDetail(label: String, value: String, modifier: Modifier = Modifier) {
    Column(modifier) {
        Text(label, color = Color.Gray, fontSize = 12.sp)
        Text(value, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
    }
}

@Composable
fun MonthlySalesTrend() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Monthly Sales Trend", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Spacer(modifier = Modifier.height(16.dp))
            // Placeholder for the bar chart
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.Bottom
            ) {
                Box(modifier = Modifier.height(60.dp).weight(1f).background(Color(0xFF388E3C), RoundedCornerShape(4.dp)))
                Spacer(modifier = Modifier.weight(0.2f))
                Box(modifier = Modifier.height(80.dp).weight(1f).background(Color(0xFF388E3C), RoundedCornerShape(4.dp)))
                Spacer(modifier = Modifier.weight(0.2f))
                Box(modifier = Modifier.height(50.dp).weight(1f).background(Color(0xFF388E3C), RoundedCornerShape(4.dp)))
                Spacer(modifier = Modifier.weight(0.2f))
                Box(modifier = Modifier.height(70.dp).weight(1f).background(Color(0xFF388E3C), RoundedCornerShape(4.dp)))
                Spacer(modifier = Modifier.weight(0.2f))
                Box(modifier = Modifier.height(90.dp).weight(1f).background(Color(0xFF388E3C), RoundedCornerShape(4.dp)))
                Spacer(modifier = Modifier.weight(0.2f))
                Box(modifier = Modifier.height(65.dp).weight(1f).background(Color(0xFF388E3C), RoundedCornerShape(4.dp)))
            }
        }
    }
}
