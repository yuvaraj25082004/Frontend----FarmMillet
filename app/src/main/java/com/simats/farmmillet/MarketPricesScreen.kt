package com.simats.farmmillet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

data class MilletPrice(val name: String, val price: String, val change: String, val trend: Int)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MarketPricesScreen(navController: NavController) {
    val milletPrices = listOf(
        MilletPrice("Finger Millet (Ragi)", "₹48", "+2.5%", 1),
        MilletPrice("Sorghum (Jowar)", "₹42", "-1.2%", -1),
        MilletPrice("Pearl Millet (Bajra)", "₹35", "0%", 0),
        MilletPrice("Foxtail Millet", "₹65", "+5.0%", 1)
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Market Prices", fontWeight = FontWeight.Bold) },
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
            InfoCard()
            Spacer(modifier = Modifier.height(16.dp))
            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(milletPrices) { millet ->
                    MilletPriceCard(millet)
                }
            }
        }
    }
}

@Composable
fun InfoCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF3E0)),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            Icon(Icons.Default.Info, contentDescription = "Info", tint = Color(0xFFFFA000))
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                "These are indicative prices. Final price may vary based on quality grade (A/B/C) and moisture content.",
                fontSize = 14.sp,
                lineHeight = 20.sp
            )
        }
    }
}

@Composable
fun MilletPriceCard(millet: MilletPrice) {
    val trendIcon = when {
        millet.trend > 0 -> Icons.Default.ArrowUpward
        millet.trend < 0 -> Icons.Default.ArrowDownward
        else -> null
    }
    val trendColor = when {
        millet.trend > 0 -> Color(0xFF388E3C)
        millet.trend < 0 -> Color(0xFFD32F2F)
        else -> Color.Gray
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(millet.name, fontWeight = FontWeight.SemiBold, fontSize = 18.sp)
                Text("Per kg", fontSize = 14.sp, color = Color.Gray)
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(millet.price, fontWeight = FontWeight.Bold, fontSize = 22.sp)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    trendIcon?.let {
                        Icon(it, contentDescription = "Trend", tint = trendColor, modifier = Modifier.size(16.dp))
                    }
                    Text("${millet.change} since last week", color = trendColor, fontSize = 12.sp)
                }
            }
        }
    }
}