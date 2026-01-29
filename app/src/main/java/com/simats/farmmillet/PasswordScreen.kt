package com.simats.farmmillet

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordScreen(navController: NavController) {

    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
        ) {

            Text("Password", fontWeight = FontWeight.Medium)

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                placeholder = { Text("Create password") },
                visualTransformation = PasswordVisualTransformation(),
                trailingIcon = {
                    Icon(Icons.Default.Visibility, contentDescription = null)
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text("Confirm Password", fontWeight = FontWeight.Medium)

            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                placeholder = { Text("Re-enter password") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(28.dp))

            Button(
                onClick = {
                    navController.navigate(AppRoutes.FARMER_DASHBOARD) {
                        popUpTo(AppRoutes.WELCOME) { inclusive = true }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1FA74A))
            ) {
                Text("Save & continue", color = Color.White)
            }

            Spacer(modifier = Modifier.height(18.dp))

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
                modifier = Modifier.align(Alignment.CenterHorizontally),
                onClick = { offset ->
                    annotatedText.getStringAnnotations(tag = "SignIn", start = offset, end = offset).firstOrNull()?.let {
                        navController.navigate(AppRoutes.FARMER_LOGIN)
                    }
                }
            )
        }
    }
}
