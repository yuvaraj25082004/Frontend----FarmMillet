package com.simats.farmmillet

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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
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
fun ConsumerLoginScreen(navController: NavController) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var emailOrMobile by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("") },
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
                .padding(24.dp)
        ) {
            Text("Welcome Back", fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Text("Enter your credentials to access your account", color = Color.Gray)
            Spacer(modifier = Modifier.height(32.dp))

            OutlinedTextField(
                value = emailOrMobile,
                onValueChange = { emailOrMobile = it },
                label = { Text("Email or Mobile Number") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                enabled = !isLoading
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
                trailingIcon = {
                    IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                        Icon(if (isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff, contentDescription = "Toggle password visibility")
                    }
                },
                enabled = !isLoading
            )
            TextButton(onClick = { navController.navigate(AppRoutes.CONSUMER_RESET_PASSWORD) }, modifier = Modifier.fillMaxWidth()) {
                Text("Forgot Password?", textAlign = TextAlign.End)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    if (emailOrMobile.isEmpty() || password.isEmpty()) {
                        Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    
                    isLoading = true
                    scope.launch {
                        try {
                            val response = RetrofitClient.instance.loginConsumer(LoginRequest(emailOrMobile, password))
                            if (response.isSuccessful && response.body()?.success == true) {
                                val loginData = response.body()?.data
                                if (loginData != null) {
                                    TokenManager.saveToken(context, loginData.token)
                                    TokenManager.saveUserDetails(context, loginData.user)
                                    RetrofitClient.setToken(loginData.token)
                                    Toast.makeText(context, "Welcome ${loginData.user.name}!", Toast.LENGTH_SHORT).show()
                                    navController.navigate(AppRoutes.CONSUMER_DASHBOARD) { 
                                        popUpTo(AppRoutes.ROLE_SELECTION) { inclusive = true } 
                                    }
                                }
                            } else {
                                Toast.makeText(context, response.body()?.message ?: "Login Failed", Toast.LENGTH_SHORT).show()
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
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Text("Login", fontSize = 16.sp)
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                Text("New user? ", color = Color.Gray)
                val annotatedText = buildAnnotatedString {
                    withStyle(style = SpanStyle(color = Color(0xFF1FA74A), fontWeight = FontWeight.SemiBold)) {
                        append("Create Account")
                    }
                }
                Text(
                    text = annotatedText,
                    modifier = Modifier.clickable { navController.navigate(AppRoutes.CONSUMER_REGISTER) }
                )
            }
        }
    }
}
