package com.simats.farmmillet

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.DomainVerification
import androidx.compose.material.icons.filled.LocationOn
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShgFpoProfileScreen(navController: NavController) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var organizationName by remember { mutableStateOf("") }
    var contactPersonName by remember { mutableStateOf("") }
    var mobileNumber by remember { mutableStateOf("") }
    var emailId by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var street by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var state by remember { mutableStateOf("") }
    var pincode by remember { mutableStateOf("") }
    var licenseNumber by remember { mutableStateOf("") }
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
            Text("Enter SHG/FPO organization details", color = Color.Gray)
            Spacer(modifier = Modifier.height(24.dp))

            SectionHeader(icon = Icons.Default.Business, title = "Organization Details")
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(value = organizationName, onValueChange = { organizationName = it }, label = { Text("Organization Name") }, modifier = Modifier.fillMaxWidth(), keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next), enabled = !isLoading)
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(value = contactPersonName, onValueChange = { contactPersonName = it }, label = { Text("Contact Person Name") }, modifier = Modifier.fillMaxWidth(), keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next), enabled = !isLoading)
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

            SectionHeader(icon = Icons.Default.DomainVerification, title = "SHG/FPO Details")
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(value = licenseNumber, onValueChange = { licenseNumber = it }, label = { Text("License / Reg Number") }, modifier = Modifier.fillMaxWidth(), keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done), enabled = !isLoading)
            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    if (emailId.isEmpty() || password.isEmpty() || organizationName.isEmpty()) {
                        Toast.makeText(context, "Required fields missing", Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    isLoading = true
                    scope.launch {
                        try {
                            val request = SHGRegisterRequest(
                                name = contactPersonName,
                                email = emailId,
                                mobile = mobileNumber,
                                password = password,
                                street = street,
                                city = city,
                                pincode = pincode,
                                organizationName = organizationName,
                                licenseNumber = licenseNumber
                            )
                            val response = RetrofitClient.instance.registerSHG(request)
                            if (response.isSuccessful && response.body()?.success == true) {
                                Toast.makeText(context, "SHG Registered! Verify OTP.", Toast.LENGTH_LONG).show()
                                navController.navigate("shg_fpo_verification_code/$emailId")
                            } else {
                                Toast.makeText(context, response.body()?.message ?: "Registration Failed", Toast.LENGTH_SHORT).show()
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
                        navController.navigate(AppRoutes.SHG_FPO_LOGIN)
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
