package com.simats.farmmillet

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConsumerEditProfileScreen(navController: NavController) {
    var fullName by remember { mutableStateOf("kjsdgi") }
    var mobileNumber by remember { mutableStateOf("") }
    var emailId by remember { mutableStateOf("ydu23@gmail.com") }
    var street by remember { mutableStateOf("sfvd, sfv") }
    var city by remember { mutableStateOf("sfvr") }
    var state by remember { mutableStateOf("") }
    var pincode by remember { mutableStateOf("889444") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Complete Profile") },
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
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Enter your personal details for delivery", color = Color.Gray)
            Spacer(modifier = Modifier.height(24.dp))

            SectionHeader(icon = Icons.Default.Person, title = "Personal Details")
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(value = fullName, onValueChange = { fullName = it }, label = { Text("Full Name") }, modifier = Modifier.fillMaxWidth(), keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next))
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(value = mobileNumber, onValueChange = { mobileNumber = it }, label = { Text("Mobile Number") }, modifier = Modifier.fillMaxWidth(), keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next))
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(value = emailId, onValueChange = { emailId = it }, label = { Text("Email ID") }, modifier = Modifier.fillMaxWidth(), keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next))
            Spacer(modifier = Modifier.height(24.dp))

            SectionHeader(icon = Icons.Default.LocationOn, title = "Delivery Address")
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(value = street, onValueChange = { street = it }, label = { Text("Street / Area") }, modifier = Modifier.fillMaxWidth(), keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next))
            Spacer(modifier = Modifier.height(16.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                OutlinedTextField(value = city, onValueChange = { city = it }, label = { Text("City") }, modifier = Modifier.weight(1f), keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next))
                OutlinedTextField(value = state, onValueChange = { state = it }, label = { Text("State") }, modifier = Modifier.weight(1f), keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next))
            }
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(value = pincode, onValueChange = { pincode = it }, label = { Text("Pincode") }, modifier = Modifier.fillMaxWidth(), keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done))
            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = { navController.popBackStack() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1FA74A))
            ) {
                Text("Next", fontSize = 16.sp)
            }
        }
    }
}

@Composable
private fun SectionHeader(icon: ImageVector, title: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Icon(icon, contentDescription = title, tint = Color.DarkGray)
        Spacer(modifier = Modifier.width(8.dp))
        Text(title, fontWeight = FontWeight.SemiBold, fontSize = 18.sp)
    }
}
