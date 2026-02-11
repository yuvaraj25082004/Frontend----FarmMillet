package com.simats.farmmillet

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

import android.widget.Toast
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMillets1upplyScreen(navController: NavController) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var milletType by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf("") }
    var qualityGrade by remember { mutableStateOf("") }
    var harvestDate by remember { mutableStateOf("") }
    var packagingDate by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var collectionBy by remember { mutableStateOf("") }
    var collectionDate by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    val milletTypes = listOf(
        "Sorghum (Jowar)", "Pearl Millet (Bajra)", "Finger Millet (Ragi)",
        "Foxtail Millet (Thinai/Kangni)", "Little Millet (Samai/Kutki)",
        "Kodo Millet (Varagu/Kodon)", "Proso Millet (Cheena/Panivaragu)",
        "Barnyard Millet (Kuthiravali/Sanwa)"
    )
    val qualityGrades = listOf("Grade A", "Grade B", "Grade C")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Millet Supply", fontWeight = FontWeight.Bold) },
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
                .padding(padding)
                .padding(horizontal = 16.dp, vertical = 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            DropdownMenuField(label = "Millet Type", options = milletTypes, selectedOption = milletType, onOptionSelected = { milletType = it })
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = quantity,
                onValueChange = { quantity = it },
                label = { Text("Quantity (kg)") },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading
            )
            Spacer(modifier = Modifier.height(16.dp))
            DateField(label = "Harvest Date", date = harvestDate, onDateChange = { harvestDate = it })
            Spacer(modifier = Modifier.height(16.dp))
            DateField(label = "Packaging Date", date = packagingDate, onDateChange = { packagingDate = it })
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = location,
                onValueChange = { location = it },
                label = { Text("Location") },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading
            )
            Spacer(modifier = Modifier.height(16.dp))
             OutlinedTextField(
                value = collectionBy,
                onValueChange = { collectionBy = it },
                label = { Text("Collection By (SHG / FPO)") },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading
            )
            Spacer(modifier = Modifier.height(16.dp))
            DateField(label = "Collection Date", date = collectionDate, onDateChange = { collectionDate = it })
            Spacer(modifier = Modifier.height(32.dp))
            Button(
                onClick = {
                    if (milletType.isEmpty() || quantity.isEmpty() || harvestDate.isEmpty() || location.isEmpty()) {
                        Toast.makeText(context, "Please fill all required fields", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    val qtyValue = quantity.toDoubleOrNull()
                    if (qtyValue == null) {
                        Toast.makeText(context, "Invalid quantity", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    isLoading = true
                    scope.launch {
                        try {
                            val request = AddSupplyRequest(
                                milletType = milletType,
                                quantityKg = qtyValue,
                                harvestDate = harvestDate, // Note: Should ideally be YYYY-MM-DD
                                packagingDate = packagingDate,
                                location = location
                            )
                            val response = RetrofitClient.instance.addSupply(request)
                            if (response.isSuccessful && response.body()?.success == true) {
                                Toast.makeText(context, "Supply added successfully", Toast.LENGTH_SHORT).show()
                                navController.navigate(AppRoutes.MY_SUPPLY_LIST)
                            } else {
                                Toast.makeText(context, response.body()?.message ?: "Failed to add supply", Toast.LENGTH_SHORT).show()
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
                    .height(50.dp),
                shape = RoundedCornerShape(25.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1FA74A)),
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Text("Add Supply", fontSize = 16.sp, color = Color.White)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownMenuField(label: String, options: List<String>, selectedOption: String, onOptionSelected: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = selectedOption,
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier.menuAnchor().fillMaxWidth()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onOptionSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateField(label: String, date: String, onDateChange: (String) -> Unit) {
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()
    val context = LocalContext.current

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    val selectedDate = datePickerState.selectedDateMillis
                    if (selectedDate != null) {
                        val sdf = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
                        onDateChange(sdf.format(java.util.Date(selectedDate)))
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
        value = date,
        onValueChange = {},
        readOnly = true,
        label = { Text(label) },
        placeholder = { Text("YYYY-MM-DD") },
        trailingIcon = {
            Icon(Icons.Default.DateRange, contentDescription = "Select Date", modifier = Modifier.clickable { showDatePicker = true })
        },
        modifier = Modifier
            .fillMaxWidth()
            .clickable { showDatePicker = true },
        enabled = false, // Make the text field disabled so clicking anywhere opens the picker
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
            .height(56.dp) // Approximate height of OutlinedTextField
            .clickable { showDatePicker = true }
    )
}
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AddMillets1upplyScreenPreview() {
    AddMillets1upplyScreen(navController = NavController(LocalContext.current))
}