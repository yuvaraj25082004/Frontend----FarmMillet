package com.simats.farmmillet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.Share
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
fun ReceiptDetailsScreen(navController: NavController, paymentId: Int) {
    var receipt by remember { mutableStateOf<ReceiptResponse?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(paymentId) {
        try {
            val response = RetrofitClient.instance.getReceiptById(paymentId)
            if (response.isSuccessful && response.body()?.success == true) {
                receipt = response.body()?.data
            } else {
                errorMessage = response.body()?.message ?: "Failed to load receipt"
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
                title = { Text("Receipt Details", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { /* TODO: Implement print/share */ }) {
                        Icon(Icons.Default.Print, contentDescription = "Print")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFFF9F9F9))
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF9F9F9))
                .padding(padding)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            if (isLoading) {
                CircularProgressIndicator(color = Color(0xFF1FA74A))
            } else if (errorMessage != null) {
                Text(errorMessage!!, color = Color.Red)
            } else {
                receipt?.let { data ->
                    ReceiptCard(data)
                }
            }
        }
    }
}

@Composable
fun ReceiptCard(data: ReceiptResponse) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = "Success",
                tint = Color(0xFF1FA74A),
                modifier = Modifier.size(64.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text("Payment Successful", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1FA74A))
            Text("Ref: #TXN-${data.id}", fontSize = 14.sp, color = Color.Gray)
            
            Spacer(modifier = Modifier.height(24.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(24.dp))

            ReceiptRow("Amount Paid", "₹${data.amount.toInt()}", true)
            ReceiptRow("Date", data.createdAt)
            ReceiptRow("Payment Method", data.paymentMethod ?: "N/A")
            
            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(16.dp))
            
            Text("Supply Details", fontWeight = FontWeight.SemiBold, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
            ReceiptRow("Millet Type", data.milletType ?: "-")
            ReceiptRow("Quantity", "${data.quantityKg ?: 0} kg")
            ReceiptRow("Quality Grade", data.qualityGrade ?: "-")

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(16.dp))

            Text("Payer Details", fontWeight = FontWeight.SemiBold, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
            ReceiptRow("Paid By", data.paidBy ?: "Unknown SHG")
            ReceiptRow("City", data.shgCity ?: "-")
            ReceiptRow("Contact", data.shgMobile ?: "-")
        }
    }
}

@Composable
fun ReceiptRow(label: String, value: String, isBold: Boolean = false) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, color = Color.Gray, fontSize = 14.sp)
        Text(
            value, 
            fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal, 
            fontSize = if (isBold) 18.sp else 14.sp,
            color = Color.Black
        )
    }
}
