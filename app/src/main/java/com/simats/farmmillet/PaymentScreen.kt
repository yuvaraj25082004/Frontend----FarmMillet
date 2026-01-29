package com.simats.farmmillet

import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.json.JSONObject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentScreen(navController: NavController, orderId: String?, amount: String?) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var isLoading by remember { mutableStateOf(false) }
    var showMockRazorpay by remember { mutableStateOf(false) }
    
    val displayAmount = amount ?: "0.00"
    val numericOrderId = orderId?.toIntOrNull() ?: 0

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Payment Methods", fontWeight = FontWeight.Bold) },
                navigationIcon = { IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back") } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        containerColor = Color(0xFFF8F9FA)
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Total Amount to Pay", color = Color.Gray)
                    Text("₹$displayAmount", fontWeight = FontWeight.Bold, fontSize = 36.sp, color = Color(0xFF1FA74A))
                    Text("Order ID: #$orderId", color = Color.Gray, fontSize = 14.sp)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            
            Text("Select Payment Method", fontWeight = FontWeight.Bold, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(16.dp))

            PaymentMethodItem("Razorpay (UPI, Card, Netbanking)", "Suggested", true) {
                showMockRazorpay = true
            }

            PaymentMethodItem("Cash on Delivery", "Available for your location", false) {
                Toast.makeText(context, "COD currently unavailable for this order", Toast.LENGTH_SHORT).show()
            }

            Spacer(modifier = Modifier.weight(1f))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Lock, contentDescription = null, modifier = Modifier.size(14.dp), tint = Color.Gray)
                Spacer(modifier = Modifier.width(4.dp))
                Text("Ssl Secured Checkout", color = Color.Gray, fontSize = 12.sp)
            }
        }
    }

    if (showMockRazorpay) {
        MockRazorpayDialog(
            amount = displayAmount,
            onSuccess = { paymentId ->
                showMockRazorpay = false
                isLoading = true
                scope.launch {
                    try {
                        val response = RetrofitClient.instance.createPaymentOrder(PaymentOrderRequest(displayAmount.toDouble(), numericOrderId))
                        if (response.isSuccessful && response.body()?.success == true) {
                            val razorOrderId = response.body()?.data?.razorpayOrderId ?: ""
                            
                            // Call verify API
                            val verifyRes = RetrofitClient.instance.verifyPayment(PaymentVerifyRequest(
                                razorpayOrderId = razorOrderId,
                                razorpayPaymentId = paymentId,
                                razorpaySignature = "mock_signature"
                            ))
                            
                            if (verifyRes.isSuccessful) {
                                Toast.makeText(context, "Payment Successful!", Toast.LENGTH_LONG).show()
                                navController.navigate("track_order/$orderId") {
                                    popUpTo(AppRoutes.CONSUMER_DASHBOARD)
                                }
                            } else {
                                Toast.makeText(context, "Verification Failed", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } catch (e: Exception) {
                        Toast.makeText(context, "Payment Error: ${e.message}", Toast.LENGTH_SHORT).show()
                    } finally {
                        isLoading = false
                    }
                }
            },
            onDismiss = { showMockRazorpay = false }
        )
    }

    if (isLoading) {
        Dialog(onDismissRequest = {}) {
            Box(modifier = Modifier.size(100.dp).background(Color.White, RoundedCornerShape(12.dp)), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color(0xFF1FA74A))
            }
        }
    }
}

@Composable
fun PaymentMethodItem(title: String, subtitle: String, recommended: Boolean, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(40.dp).background(Color(0xFFF0F0F0), RoundedCornerShape(8.dp)), contentAlignment = Alignment.Center) {
                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = if (recommended) Color(0xFF1976D2) else Color.Gray)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(title, fontWeight = FontWeight.Bold)
                Text(subtitle, color = Color.Gray, fontSize = 12.sp)
            }
        }
    }
}

@Composable
fun MockRazorpayDialog(amount: String, onSuccess: (String) -> Unit, onDismiss: () -> Unit) {
    var isProcessing by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color(0xFFF3F6F9)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Razorpay Header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF2B3A4C))
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Millet Marketplace", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Text("Amount: ₹$amount", color = Color.LightGray, fontSize = 14.sp)
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = null, tint = Color.White)
                    }
                }

                if (!isProcessing) {
                    Column(modifier = Modifier.padding(24.dp)) {
                        Text("Preferred Payment Methods", fontWeight = FontWeight.Bold, color = Color.DarkGray)
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        MockPayOption("UPI - Google Pay / PhonePe", "⚡ Fast & Secure")
                        MockPayOption("Cards (Visa, MaterCard, RuPay)", "Add new card")
                        MockPayOption("Netbanking", "All Indian banks")
                        
                        Spacer(modifier = Modifier.height(32.dp))
                        
                        Button(
                            onClick = { 
                                isProcessing = true
                                scope.launch {
                                    delay(2000)
                                    onSuccess("pay_demo_" + System.currentTimeMillis())
                                }
                            },
                            modifier = Modifier.fillMaxWidth().height(54.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3399FF)),
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text("PAY ₹$amount", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        }
                    }
                } else {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator(color = Color(0xFF3399FF))
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Processing Payment...", fontWeight = FontWeight.Medium)
                        Text("Please do not press back or close the app", color = Color.Gray, fontSize = 12.sp)
                    }
                }
                
                Spacer(modifier = Modifier.weight(1f))
                
                // Razorpay Footer
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Trusted by 10 million+ businesses", color = Color.Gray, fontSize = 10.sp)
                }
            }
        }
    }
}

@Composable
fun MockPayOption(title: String, subtitle: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.size(32.dp).background(Color(0xFFE8F0FE), RoundedCornerShape(4.dp)))
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(title, fontWeight = FontWeight.Medium)
            Text(subtitle, color = Color.Gray, fontSize = 12.sp)
        }
    }
    HorizontalDivider(color = Color(0xFFEEEEEE))
}
