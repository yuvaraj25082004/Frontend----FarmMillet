package com.simats.farmmillet

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddProductListingScreen(
    navController: NavController,
    argSupplyId: String? = null,
    argMilletType: String? = null,
    argQuantity: String? = null,
    argQualityGrade: String? = null
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    
    var milletType by remember { mutableStateOf(argMilletType ?: "Finger Millet (Ragi)") }
    var quantity by remember { mutableStateOf(argQuantity ?: "500") }
    var price by remember { mutableStateOf("") }
    var qualityGrade by remember { mutableStateOf(argQualityGrade ?: "Grade A") }
    var packagingDate by remember { mutableStateOf("") }
    var sourceFarmer by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Product Listing", fontWeight = FontWeight.Bold) },
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
                .verticalScroll(rememberScrollState())
        ) {
            OutlinedTextField(value = milletType, onValueChange = {}, label = { Text("Millet Type") }, modifier = Modifier.fillMaxWidth(), readOnly = true, trailingIcon = { Icon(Icons.Default.ExpandMore, null) })
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(value = quantity, onValueChange = { quantity = it }, label = { Text("Quantity (kg)") }, modifier = Modifier.fillMaxWidth(), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next), enabled = !isLoading)
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(value = price, onValueChange = { price = it }, label = { Text("Price (₹/kg)") }, placeholder = { Text("e.g. 60") }, modifier = Modifier.fillMaxWidth(), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next), enabled = !isLoading)
            Text("Suggested Market Price: ₹55 - ₹65", color = Color.Gray, style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(start = 16.dp, top = 4.dp))
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(value = qualityGrade, onValueChange = {}, label = { Text("Quality Grade") }, modifier = Modifier.fillMaxWidth(), readOnly = true, trailingIcon = { Icon(Icons.Default.ExpandMore, null) })
            Spacer(modifier = Modifier.height(16.dp))
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
                                packagingDate = sdf.format(java.util.Date(selectedDate))
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

            OutlinedTextField(
                value = packagingDate,
                onValueChange = {},
                readOnly = true,
                label = { Text("Packaging Date (YYYY-MM-DD)") },
                placeholder = { Text("YYYY-MM-DD") },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showDatePicker = true },
                trailingIcon = {
                    Icon(Icons.Default.CalendarToday, contentDescription = "Select Date", modifier = Modifier.clickable { showDatePicker = true })
                },
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
            // Overlay a transparent box to capture clicks if enabled=false prevents interaction
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .clickable { showDatePicker = true }
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(value = sourceFarmer, onValueChange = { sourceFarmer = it }, label = { Text("Source Farmer (Optional)") }, modifier = Modifier.fillMaxWidth(), keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next), enabled = !isLoading)
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(value = description, onValueChange = { description = it }, label = { Text("Product Description") }, placeholder = { Text("Describe the product quality and benefits...") }, modifier = Modifier.fillMaxWidth().height(120.dp), maxLines = 4, enabled = !isLoading)
            Spacer(modifier = Modifier.height(24.dp))
            DemandIndicator()
            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = {
                    if (price.isEmpty() || packagingDate.isEmpty()) {
                        Toast.makeText(context, "Please fill all required fields", Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    val priceVal = price.toDoubleOrNull()
                    val qtyVal = quantity.toDoubleOrNull()
                    if (priceVal == null || qtyVal == null) {
                        Toast.makeText(context, "Invalid input values", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    isLoading = true
                    scope.launch {
                        try {
                            val request = CreateProductRequest(
                                supplyId = argSupplyId?.toIntOrNull(),
                                milletType = milletType,
                                quantityKg = qtyVal,
                                pricePerKg = priceVal,
                                qualityGrade = qualityGrade.replace("Grade ", ""),
                                packagingDate = packagingDate,
                                sourceFarmerName = sourceFarmer,
                                description = description
                            )
                            val response = RetrofitClient.instance.createProduct(request)
                            if (response.isSuccessful && response.body()?.success == true) {
                                Toast.makeText(context, "Product listed successfully!", Toast.LENGTH_SHORT).show()
                                navController.navigate(AppRoutes.MY_PRODUCTS) {
                                    popUpTo(AppRoutes.SHG_FPO_DASHBOARD)
                                }
                            } else {
                                Toast.makeText(context, response.body()?.message ?: "Listing Failed", Toast.LENGTH_SHORT).show()
                            }
                        } catch (e: Exception) {
                            Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                        } finally {
                            isLoading = false
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1FA74A)),
                shape = RoundedCornerShape(12.dp),
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Text("List Product", fontSize = 16.sp)
                }
            }
        }
    }
}

@Composable
private fun DemandIndicator() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF3E0)),
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.Info, contentDescription = null, tint = Color(0xFFFFA000))
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text("Consumer Demand: High 🔥", fontWeight = FontWeight.SemiBold, color = Color(0xFFFFA000))
                Text("This millet type is currently trending in your region.", fontSize = 12.sp, color = Color.Gray, lineHeight = 18.sp)
            }
        }
    }
}
