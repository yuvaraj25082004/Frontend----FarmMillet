package com.simats.farmmillet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import androidx.compose.runtime.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(navController: NavController) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var isLoading by remember { mutableStateOf(false) }
    
    val cartSubtotal = CartManager.getSubtotal()
    val deliveryFee = 20.0
    val totalToPay = cartSubtotal + deliveryFee
    
    // Get user address from TokenManager
    val userCity = remember { TokenManager.getUserCity(context) }
    val userStreet = remember { TokenManager.getUserStreet(context) }
    val defaultAddress = if (userStreet.isNotEmpty() && userCity.isNotEmpty()) "$userStreet, $userCity" else "Please set your address in profile"

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Checkout", fontWeight = FontWeight.Bold) },
                navigationIcon = { IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back") } },
                actions = {
                    IconButton(onClick = { navController.navigate(AppRoutes.CONSUMER_NOTIFICATIONS) }) { Icon(Icons.Default.Notifications, "Notifications") }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        bottomBar = {
            Button(
                onClick = {
                    if (defaultAddress.startsWith("Please")) {
                        Toast.makeText(context, "Please set delivery address in profile", Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    if (CartManager.items.isEmpty()) {
                        Toast.makeText(context, "Your cart is empty", Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    
                    isLoading = true
                    scope.launch {
                        try {
                            // Map cart items to OrderItemRequest
                            val orderItems = CartManager.items.map { 
                                OrderItemRequest(productId = it.id, quantityKg = it.quantity.toDouble())
                            }
                            
                            val request = OrderRequest(
                                items = orderItems,
                                dropoffLocation = defaultAddress
                            )
                            val response = RetrofitClient.instance.placeOrder(request)
                            if (response.isSuccessful && response.body()?.success == true) {
                                // Clear cart after success
                                CartManager.clearCart()
                                Toast.makeText(context, "Order Placed Successfully!", Toast.LENGTH_SHORT).show()
                                val orderId = response.body()?.data?.orderId ?: 0
                                navController.navigate("payment/$orderId/$totalToPay")
                            } else {
                                Toast.makeText(context, response.body()?.message ?: "Order Failed", Toast.LENGTH_SHORT).show()
                            }
                        } catch (e: Exception) {
                            Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                        } finally {
                            isLoading = false
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1FA74A)),
                enabled = !isLoading
            ) {
                if (isLoading) {
                    androidx.compose.material3.CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Text("Pay ₹%.2f".format(totalToPay), fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }
        },
        containerColor = Color(0xFFF8F9FA)
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Text("Delivery Address", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Spacer(modifier = Modifier.height(16.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.LocationOn, contentDescription = "Location", tint = Color(0xFF1FA74A))
                    Spacer(modifier = Modifier.padding(8.dp))
                    Column {
                        Text("Default Address", fontWeight = FontWeight.SemiBold)
                        Text(defaultAddress, color = Color.Gray, fontSize = 14.sp)
                        TextButton(onClick = { navController.navigate(AppRoutes.CONSUMER_EDIT_PROFILE) }) {
                            Text("Edit Address")
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            Text("Order Summary", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Spacer(modifier = Modifier.height(16.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(selected = true, onClick = { /* Always UPI for now */ })
                        Text("Pay with UPI / Card")
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    HorizontalDivider(color = Color(0xFFF0F0F0))
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Subtotal (${CartManager.items.size} items)", color = Color.Gray)
                        Text("₹%.2f".format(cartSubtotal))
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Delivery Fee", color = Color.Gray)
                        Text("₹%.2f".format(deliveryFee))
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Total Amount", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Text("₹%.2f".format(totalToPay), fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color(0xFF1FA74A))
                    }
                }
            }
        }
    }
}
