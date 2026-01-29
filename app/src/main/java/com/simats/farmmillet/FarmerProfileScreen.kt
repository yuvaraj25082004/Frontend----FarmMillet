package com.simats.farmmillet

import android.util.Log
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FarmerProfileScreen(navController: NavController) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var fullName by remember { mutableStateOf("") }
    var mobileNumber by remember { mutableStateOf("") }
    var emailId by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var street by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var state by remember { mutableStateOf("") }
    var pincode by remember { mutableStateOf("") }
    var farmLocation by remember { mutableStateOf("") }
    var accountNumber by remember { mutableStateOf("") }
    var ifscCode by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Complete Profile") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
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
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Enter your farm and personal details", color = Color.Gray)
            Spacer(modifier = Modifier.height(24.dp))

            SectionHeader(icon = Icons.Default.Person, title = "Personal Details")
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(value = fullName, onValueChange = { fullName = it }, label = { Text("Full Name") }, modifier = Modifier.fillMaxWidth(), keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next), enabled = !isLoading)
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(value = mobileNumber, onValueChange = { mobileNumber = it }, label = { Text("Mobile Number") }, modifier = Modifier.fillMaxWidth(), keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next), enabled = !isLoading)
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(value = emailId, onValueChange = { emailId = it }, label = { Text("Email ID") }, modifier = Modifier.fillMaxWidth(), keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next), enabled = !isLoading)
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(value = password, onValueChange = { password = it }, label = { Text("Password") }, modifier = Modifier.fillMaxWidth(), visualTransformation = PasswordVisualTransformation(), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Next), enabled = !isLoading)
            Spacer(modifier = Modifier.height(24.dp))

            SectionHeader(icon = Icons.Default.LocationOn, title = "Address Details")
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(value = street, onValueChange = { street = it }, label = { Text("Street / Area") }, modifier = Modifier.fillMaxWidth(), keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next), enabled = !isLoading)
            Spacer(modifier = Modifier.height(16.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                OutlinedTextField(value = city, onValueChange = { city = it }, label = { Text("City") }, modifier = Modifier.weight(1f), keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next), enabled = !isLoading)
                OutlinedTextField(value = state, onValueChange = { state = it }, label = { Text("State") }, modifier = Modifier.weight(1f), keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next), enabled = !isLoading)
            }
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(value = pincode, onValueChange = { pincode = it }, label = { Text("Pincode") }, modifier = Modifier.fillMaxWidth(), keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next), enabled = !isLoading)
            Spacer(modifier = Modifier.height(24.dp))

            SectionHeader(icon = Icons.Default.Eco, title = "Farm Details")
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(value = farmLocation, onValueChange = { farmLocation = it }, label = { Text("Farm Location") }, modifier = Modifier.fillMaxWidth(), keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next), enabled = !isLoading)
            Spacer(modifier = Modifier.height(24.dp))

            SectionHeader(icon = Icons.Default.AccountBalance, title = "Bank Details")
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(value = accountNumber, onValueChange = { accountNumber = it }, label = { Text("Account Number") }, modifier = Modifier.fillMaxWidth(), keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next), enabled = !isLoading)
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(value = ifscCode, onValueChange = { ifscCode = it }, label = { Text("IFSC Code") }, modifier = Modifier.fillMaxWidth(), keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done), enabled = !isLoading)
            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    if (emailId.isEmpty() || password.isEmpty() || fullName.isEmpty()) {
                        Toast.makeText(context, "Required fields missing", Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    isLoading = true
                    scope.launch {
                        try {
                            val request = FarmerRegisterRequest(
                                name = fullName,
                                email = emailId,
                                mobile = mobileNumber,
                                password = password,
                                street = street,
                                city = city,
                                pincode = pincode,
                                farmLocation = farmLocation,
                                bankAccountNumber = accountNumber,
                                bankIfscCode = ifscCode
                            )
                            val response = RetrofitClient.instance.registerFarmer(request)
                            if (response.isSuccessful && response.body()?.success == true) {
                                Toast.makeText(context, "Registration Successful! Please verify.", Toast.LENGTH_LONG).show()
                                navController.navigate("farmer_verification_code/$emailId")
                            } else {
                                Toast.makeText(context, response.body()?.message ?: "Registration Failed", Toast.LENGTH_SHORT).show()
                                Log.d("Registration Error", response.body()?.message ?: "Registration Failed")
                            }
                        } catch (e: Exception) {
                            Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                            Log.d("Registration Error", e.toString())
                        } finally {
                            isLoading = false
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1FA74A)),
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Text("Save & continue", fontSize = 16.sp)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            val annotatedText = buildAnnotatedString {
                append("Already have an account? ")
                withStyle(
                    SpanStyle(
                        color = Color(0xFF1FA74A),
                        fontWeight = FontWeight.Bold
                    )
                ) {
                    pushStringAnnotation(tag = "SignIn", annotation = "SignIn")
                    append("Sign In")
                    pop()
                }
            }

            ClickableText(
                text = annotatedText,
                onClick = {
                        offset ->
                    annotatedText.getStringAnnotations(tag = "SignIn", start = offset, end = offset).firstOrNull()?.let {
                        navController.navigate(AppRoutes.FARMER_LOGIN)
                    }
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun SectionHeader(icon: ImageVector, title: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Icon(imageVector = icon, contentDescription = title, tint = Color.DarkGray)
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = title, fontWeight = FontWeight.SemiBold, fontSize = 18.sp)
    }
}
