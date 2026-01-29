package com.simats.farmmillet

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

private data class AnalyticsProduct(val name: String, val price: String, val stock: String, val listings: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShgAnalyticsScreen(navController: NavController) {
    val products = listOf(
        AnalyticsProduct("Finger Millet (Ragi)", "₹45/kg", "100 kg stock", "1 listings"),
        AnalyticsProduct("Pearl Millet (Bajra)", "₹38/kg", "50 kg stock", "1 listings"),
        AnalyticsProduct("Foxtail Millet (Thinai/Kangni)", "₹60/kg", "75 kg stock", "1 listings")
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Analytics", fontWeight = FontWeight.Bold) },
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
            SummaryGrid()
            Spacer(modifier = Modifier.height(24.dp))
            Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(Color.White), elevation = CardDefaults.cardElevation(2.dp)){
                Column(modifier = Modifier.padding(16.dp)){
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Product Performance", fontWeight = FontWeight.SemiBold, fontSize = 18.sp, modifier = Modifier.weight(1f))
                        Icon(Icons.Default.ArrowUpward, contentDescription = "Sort", tint = Color.Gray)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.height(300.dp)) {
                        items(products) { product ->
                            ProductPerformanceItem(product)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SummaryGrid() {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            AnalyticsSummaryCard("Products", "3", Modifier.weight(1f))
            AnalyticsSummaryCard("Revenue", "₹0.3k", Modifier.weight(1f))
        }
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            AnalyticsSummaryCard("Active Orders", "1", Modifier.weight(1f))
            AnalyticsSummaryCard("Avg Rating", "4.5", Modifier.weight(1f), true)
        }
    }
}

@Composable
private fun AnalyticsSummaryCard(label: String, value: String, modifier: Modifier = Modifier, showStar: Boolean = false) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(label, fontSize = 14.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(value, fontWeight = FontWeight.Bold, fontSize = 22.sp)
                if (showStar) {
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFFFC107), modifier = Modifier.size(18.dp))
                }
            }
        }
    }
}

@Composable
private fun ProductPerformanceItem(product: AnalyticsProduct) {
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Column(modifier = Modifier.weight(1f)) {
            Text(product.name, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
            Text(product.listings, color = Color.Gray, fontSize = 12.sp)
        }
        Column(horizontalAlignment = Alignment.End) {
            Text(product.price, fontWeight = FontWeight.Bold, color = Color(0xFF1FA74A), fontSize = 16.sp)
            Text(product.stock, color = Color.Gray, fontSize = 12.sp)
        }
    }
}

