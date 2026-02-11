package com.simats.farmmillet

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.TrendingDown
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

data class Transaction(
    val type: String,
    val id: String,
    val date: String,
    val amount: String,
    val isCredit: Boolean
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentHistoryScreen(navController: NavController) {
    var transactions by remember { mutableStateOf<List<TransactionResponse>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var totalEarnings by remember { mutableStateOf(0.0) }

    LaunchedEffect(Unit) {
        try {
            val response = RetrofitClient.instance.getFarmerPaymentHistory()
            if (response.isSuccessful && response.body()?.success == true) {
                transactions = response.body()?.data ?: emptyList()
                totalEarnings = transactions.filter { it.status == "success" }.sumOf { it.amount }
            }
        } catch (e: Exception) {
            // Handle error
        } finally {
            isLoading = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Receipts", fontWeight = FontWeight.Bold) },
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
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFFF9F9F9))
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
            TotalEarningsCard(totalEarnings)
            Spacer(modifier = Modifier.height(24.dp))
            Text("Recent Transactions", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))
            if (isLoading) {
                CircularProgressIndicator(color = Color(0xFF1FA74A))
            } else if (transactions.isEmpty()) {
                Text("No transactions found", color = Color.Gray)
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    val successfulTransactions = transactions.filter { it.status == "success" }
                    items(successfulTransactions) { transaction ->
                        PaymentTransactionItem(transaction) {
                            navController.navigate(AppRoutes.RECEIPT_DETAILS.replace("{paymentId}", transaction.id.toString()))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TotalEarningsCard(amount: Double) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text("Total Earnings", fontSize = 16.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(8.dp))
            Text("₹${amount.toInt()}", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1FA74A))
            Spacer(modifier = Modifier.height(4.dp))
            Text("Reflected confirmed payments", fontSize = 14.sp, color = Color.Gray)
        }
    }
}

@Composable
fun PaymentTransactionItem(transaction: TransactionResponse, onClick: () -> Unit) {
    val isCredit = transaction.paymentType == "farmer_payment" || transaction.status == "success"
    val icon = if (isCredit) Icons.AutoMirrored.Filled.TrendingDown else Icons.AutoMirrored.Filled.TrendingUp
    val iconColor = if (isCredit) Color(0xFF1FA74A) else Color(0xFFD32F2F)
    val iconBackgroundColor = if (isCredit) Color(0xFFE8F5E9) else Color(0xFFFFEBEE)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(iconBackgroundColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = transaction.paymentType, tint = iconColor)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text("TXN-${transaction.id}", fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                Text(
                    text = "${transaction.createdAt.split(" ")[0]} • ${transaction.milletType ?: "Supply"}", 
                    color = Color.Gray, 
                    fontSize = 14.sp
                )
            }
            Text(
                text = "₹${transaction.amount.toInt()}",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = iconColor
            )
        }
    }
}
