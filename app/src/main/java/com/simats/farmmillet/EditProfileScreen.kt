package com.simats.farmmillet

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(navController: NavController) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val scope = rememberCoroutineScope()
    
    // Initial values from TokenManager
    var name by remember { mutableStateOf(TokenManager.getUserName(context)) }
    var email by remember { mutableStateOf(TokenManager.getUserEmail(context)) }
    var street by remember { mutableStateOf(TokenManager.getUserStreet(context)) }
    var city by remember { mutableStateOf(TokenManager.getUserCity(context)) }
    var pincode by remember { mutableStateOf("") } // Pincode not explicitly stored in separate getter yet but we can get it from saveUserDetails
    
    var isLoading by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Profile") },
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
        ) {
            Spacer(Modifier.height(16.dp))

            OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Full Name") }, modifier = Modifier.fillMaxWidth(), enabled = !isLoading)
            Spacer(Modifier.height(16.dp))
            OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email ID (Read Only)") }, modifier = Modifier.fillMaxWidth(), enabled = false)
            Spacer(Modifier.height(16.dp))
            OutlinedTextField(value = street, onValueChange = { street = it }, label = { Text("Street / Area") }, modifier = Modifier.fillMaxWidth(), enabled = !isLoading)
            Spacer(Modifier.height(16.dp))
            OutlinedTextField(value = city, onValueChange = { city = it }, label = { Text("City") }, modifier = Modifier.fillMaxWidth(), enabled = !isLoading)
            Spacer(Modifier.height(16.dp))
            OutlinedTextField(value = pincode, onValueChange = { pincode = it }, label = { Text("Pincode") }, modifier = Modifier.fillMaxWidth(), enabled = !isLoading)

            Spacer(modifier = Modifier.height(32.dp))
            Button(
                onClick = {
                    if (name.isEmpty() || street.isEmpty() || city.isEmpty()) {
                        android.widget.Toast.makeText(context, "Please fill required fields", android.widget.Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    
                    isLoading = true
                    scope.launch {
                        try {
                            val updateMap = mutableMapOf(
                                "name" to name,
                                "street" to street,
                                "city" to city
                            )
                            if (pincode.isNotEmpty()) updateMap["pincode"] = pincode
                            
                            val response = RetrofitClient.instance.updateConsumerProfile(updateMap)
                            if (response.isSuccessful && response.body()?.success == true) {
                                // Update local TokenManager
                                // For simplicity, we just trigger a navigate back, 
                                // ideally we should fetch updated user from backend again or update it manually
                                val updatedUser = UserData(
                                    id = 0,
                                    name = name,
                                    email = email,
                                    role = "consumer",
                                    city = city,
                                    street = street,
                                    mobile = "",
                                    pincode = pincode
                                )
                                TokenManager.saveUserDetails(context, updatedUser)
                                
                                android.widget.Toast.makeText(context, "Profile Updated!", android.widget.Toast.LENGTH_SHORT).show()
                                navController.popBackStack()
                            } else {
                                android.widget.Toast.makeText(context, response.body()?.message ?: "Update Failed", android.widget.Toast.LENGTH_SHORT).show()
                            }
                        } catch (e: Exception) {
                            android.widget.Toast.makeText(context, "Error: ${e.message}", android.widget.Toast.LENGTH_SHORT).show()
                        } finally {
                            isLoading = false
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth().height(52.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1FA74A)),
                shape = RoundedCornerShape(12.dp),
                enabled = !isLoading
            ) {
                if (isLoading) {
                    androidx.compose.material3.CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Text("Save Changes", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

