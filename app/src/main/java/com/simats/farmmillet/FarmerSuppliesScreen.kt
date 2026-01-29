package com.simats.farmmillet

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.LocationOn
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

import androidx.compose.runtime.*
import kotlinx.coroutines.launch
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FarmerSuppliesScreen(navController: NavController) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var supplies by remember { mutableStateOf<List<SupplyResponse>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    
    var showAcceptDialog by remember { mutableStateOf(false) }
    var selectedSupplyId by remember { mutableStateOf<Int?>(null) }
    var collectionBy by remember { mutableStateOf("") }
    var collectionDate by remember { mutableStateOf("") }
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    val selectedDate = datePickerState.selectedDateMillis
                    if (selectedDate != null) {
                        val sdf = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
                        collectionDate = sdf.format(java.util.Date(selectedDate))
                    }
                    showDatePicker = false
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    LaunchedEffect(Unit) {
        try {
            val response = RetrofitClient.instance.getAllSupplies()
            if (response.isSuccessful && response.body()?.success == true) {
                supplies = response.body()?.data ?: emptyList()
            } else {
                errorMessage = response.body()?.message ?: "Failed to fetch supplies"
            }
        } catch (e: Exception) {
            errorMessage = "Error: ${e.message}"
        } finally {
            isLoading = false
        }
    }

    var selectedGrade by remember { mutableStateOf("A") }
    val grades = listOf("A", "B", "C")

    if (showAcceptDialog) {
        AlertDialog(
            onDismissRequest = { showAcceptDialog = false },
            title = { Text("Assign Logistics & Grade") },
            text = {
                Column {
                    OutlinedTextField(
                        value = collectionBy,
                        onValueChange = { collectionBy = it },
                        label = { Text("Logistics Partner / Driver Name") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = collectionDate,
                        onValueChange = {},
                        label = { Text("Collection Date (YYYY-MM-DD)") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showDatePicker = true },
                        enabled = false,
                        colors = OutlinedTextFieldDefaults.colors(
                            disabledTextColor = MaterialTheme.colorScheme.onSurface,
                            disabledBorderColor = MaterialTheme.colorScheme.outline,
                            disabledLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            disabledTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            disabledPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                    // Overlay a transparent box to capture clicks
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .clickable { showDatePicker = true }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Quality Grade Assessment:", fontWeight = FontWeight.SemiBold)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        grades.forEach { grade ->
                            RadioButton(
                                selected = (selectedGrade == grade),
                                onClick = { selectedGrade = grade }
                            )
                            Text(
                                text = grade,
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.padding(end = 8.dp)
                            )
                        }
                    }
                }
            },
            confirmButton = {
                Button(onClick = {
                    val id = selectedSupplyId ?: return@Button
                    showAcceptDialog = false
                    isLoading = true
                    scope.launch {
                        try {
                            val request = AcceptSupplyRequest(collectionBy, collectionDate, selectedGrade)
                            val acceptResponse = RetrofitClient.instance.acceptSupply(id, request)
                            if (acceptResponse.isSuccessful && acceptResponse.body()?.success == true) {
                                Toast.makeText(context, "Supply accepted with Grade $selectedGrade!", Toast.LENGTH_SHORT).show()
                                val refreshResponse = RetrofitClient.instance.getAllSupplies()
                                if (refreshResponse.isSuccessful) supplies = refreshResponse.body()?.data ?: emptyList()
                            } else {
                                Toast.makeText(context, acceptResponse.body()?.message ?: "Failed to accept", Toast.LENGTH_SHORT).show()
                            }
                        } catch (e: Exception) {
                            Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                        } finally {
                            isLoading = false
                        }
                    }
                }) {
                    Text("Accept & Assign")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAcceptDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    var showPaymentDialog by remember { mutableStateOf(false) }
    var paymentAmount by remember { mutableStateOf("") }
    var paymentMethod by remember { mutableStateOf("Cash") }
    var selectedFarmerDetails by remember { mutableStateOf<Pair<Int, String>?>(null) } // ID, Name

    var showMockRazorpay by remember { mutableStateOf(false) }

    if (showPaymentDialog) {
        AlertDialog(
            onDismissRequest = { showPaymentDialog = false },
            title = { Text("Pay Farmer") },
            text = {
                Column {
                    Text("Paying: ${selectedFarmerDetails?.second ?: "Farmer"}", fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        value = paymentAmount,
                        onValueChange = { paymentAmount = it },
                        label = { Text("Amount (₹)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Payment Method:", fontSize = 14.sp, color = Color.Gray)
                    Row {
                        listOf("Cash", "Online (Gateway)").forEach { method ->
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                RadioButton(
                                    selected = (paymentMethod == method),
                                    onClick = { paymentMethod = method }
                                )
                                Text(method, fontSize = 12.sp, modifier = Modifier.padding(end = 8.dp))
                            }
                        }
                    }
                }
            },
            confirmButton = {
                Button(onClick = {
                    val farmerId = selectedFarmerDetails?.first ?: return@Button
                    val amount = paymentAmount.toDoubleOrNull()
                    if (amount == null || amount <= 0) {
                        Toast.makeText(context, "Invalid Amount", Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    
                    showPaymentDialog = false
                    
                    if (paymentMethod == "Online (Gateway)") {
                        showMockRazorpay = true
                    } else {
                        // Cash Payment
                        isLoading = true
                        scope.launch {
                            try {
                                val request = RecordPaymentRequest(farmerId, amount, paymentMethod, selectedSupplyId)
                                val response = RetrofitClient.instance.recordPayment(request)
                                if (response.isSuccessful && response.body()?.success == true) {
                                    Toast.makeText(context, "Payment Recorded Successfully!", Toast.LENGTH_SHORT).show()
                                    paymentAmount = ""
                                } else {
                                    Toast.makeText(context, response.body()?.message ?: "Payment Failed", Toast.LENGTH_SHORT).show()
                                }
                            } catch (e: Exception) {
                                Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                            } finally {
                                isLoading = false
                            }
                        }
                    }
                }) {
                    Text(if (paymentMethod == "Online (Gateway)") "Pay Now" else "Record Payment")
                }
            },
            dismissButton = {
                TextButton(onClick = { showPaymentDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    if (showMockRazorpay) {
        MockRazorpayDialog(
            amount = paymentAmount,
            onSuccess = { paymentId ->
                showMockRazorpay = false
                val farmerId = selectedFarmerDetails?.first ?: return@MockRazorpayDialog
                val amount = paymentAmount.toDoubleOrNull() ?: 0.0
                
                isLoading = true
                scope.launch {
                    try {
                        val request = RecordPaymentRequest(farmerId, amount, "Online - $paymentId", selectedSupplyId)
                        val response = RetrofitClient.instance.recordPayment(request)
                        if (response.isSuccessful && response.body()?.success == true) {
                            Toast.makeText(context, "Payment Successful!", Toast.LENGTH_SHORT).show()
                            paymentAmount = ""
                        } else {
                            Toast.makeText(context, response.body()?.message ?: "Payment Failed", Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: Exception) {
                        Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                    } finally {
                        isLoading = false
                    }
                }
            },
            onDismiss = { showMockRazorpay = false }
        )
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Farmer Supplies", fontWeight = FontWeight.Bold) },
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
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center), color = Color(0xFF1FA74A))
            } else if (errorMessage != null) {
                val error = errorMessage ?: "Unknown error"
                Text(error, modifier = Modifier.align(Alignment.Center), color = Color.Red)
            } else if (supplies.isEmpty()) {
                Text("No available supplies", modifier = Modifier.align(Alignment.Center), color = Color.Gray)
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(supplies) { supply ->
                        SupplyItemCard(
                            navController = navController,
                            supply = supply,
                            context = context,
                            scope = scope,
                            onRefresh = { 
                                scope.launch {
                                    val refreshRes = RetrofitClient.instance.getAllSupplies()
                                    if (refreshRes.isSuccessful) supplies = refreshRes.body()?.data ?: emptyList()
                                }
                            },
                            onAccept = {
                                selectedSupplyId = supply.id
                                showAcceptDialog = true
                            },
                            onPay = {
                                selectedFarmerDetails = Pair(supply.farmerId, supply.farmerName ?: "Farmer")
                                selectedSupplyId = supply.id
                                showPaymentDialog = true
                            }
                        )
                    }
                }
            }
        }
    }
}



@Composable
private fun SupplyItemCard(
    navController: NavController,
    supply: SupplyResponse,
    context: android.content.Context,
    scope: kotlinx.coroutines.CoroutineScope,
    onRefresh: () -> Unit,
    onAccept: () -> Unit,
    onPay: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // ... (Header parts are same, just skipping lines here to be concise in replacement if possible, but replace tool needs context)
            // Ideally I should replace the whole function body to be safe or use precise matching.
            // I'll replace from the Card content start.
            Row(verticalAlignment = Alignment.Top) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(supply.milletType, fontWeight = FontWeight.SemiBold, fontSize = 18.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Person, contentDescription = null, modifier = Modifier.size(16.dp), tint = Color.Gray)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(supply.farmerName ?: "Farmer", color = Color.Gray, fontSize = 14.sp)
                    }
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text("${supply.quantityKg} kg", fontWeight = FontWeight.Bold, color = Color(0xFF1FA74A), fontSize = 16.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = if (supply.status == "pending") Color(0xFFE3F2FD) else Color(0xFFE8F5E9),
                    ) {
                        Text(supply.status, color = if (supply.status == "pending") Color(0xFF0D47A1) else Color(0xFF2E7D32), modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp), fontSize = 12.sp)
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Grade: ${supply.qualityGrade}", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)){
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.LocationOn, contentDescription = null, modifier = Modifier.size(16.dp), tint = Color.Gray)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Managed by Marketplace", color = Color.Gray, fontSize = 14.sp)
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            if (supply.status == "pending") {
                Button(
                    onClick = { onAccept() },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1FA74A)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Accept Supply", fontSize = 16.sp)
                }
            } else if (supply.status == "accepted") {
                Button(
                    onClick = { 
                        scope.launch {
                            try {
                                val response = RetrofitClient.instance.completeSupply(supply.id)
                                if (response.isSuccessful && response.body()?.success == true) {
                                    Toast.makeText(context, "Supply marked as collected!", Toast.LENGTH_SHORT).show()
                                    onRefresh()
                                } else {
                                    Toast.makeText(context, response.body()?.message ?: "Failed to update", Toast.LENGTH_SHORT).show()
                                }
                            } catch (e: Exception) {
                                Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFA000)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Mark as Collected", fontSize = 16.sp)
                }
                
                if (supply.paymentStatus != "paid") {
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = { onPay() },
                        modifier = Modifier.fillMaxWidth().height(50.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF757575)), 
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Pay Farmer", fontSize = 16.sp)
                    }
                } else {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("Payment Completed ✅", color = Color(0xFF1FA74A), fontWeight = FontWeight.Bold, modifier = Modifier.align(Alignment.CenterHorizontally))
                }
            } else if (supply.status == "completed") {
                 Button(
                    onClick = { 
                        val route = "add_product_listing?supplyId=${supply.id}&milletType=${supply.milletType}&quantity=${supply.quantityKg}&qualityGrade=Grade ${supply.qualityGrade}"
                        navController.navigate(route) 
                    },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1976D2)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Create Product Listing", fontSize = 16.sp)
                }

                if (supply.paymentStatus != "paid") {
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = { onPay() },
                        modifier = Modifier.fillMaxWidth().height(50.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF757575)), 
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Pay Farmer", fontSize = 16.sp)
                    }
                } else {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("Payment Completed ✅", color = Color(0xFF1FA74A), fontWeight = FontWeight.Bold, modifier = Modifier.align(Alignment.CenterHorizontally))
                }
            }
        }
    }
}
