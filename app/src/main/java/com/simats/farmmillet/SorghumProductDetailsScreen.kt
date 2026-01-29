package com.simats.farmmillet

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SorghumProductDetailsScreen(navController: NavController) {
    var quantity by remember { mutableStateOf(1) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Product Details", fontWeight = FontWeight.Bold) },
                navigationIcon = { IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back") } },
                actions = {
                    IconButton(onClick = { navController.navigate(AppRoutes.CONSUMER_NOTIFICATIONS) }) { Icon(Icons.Default.Notifications, "Notifications") }
                    IconButton(onClick = { navController.navigate(AppRoutes.CONSUMER_USER_PROFILE) }) { Icon(Icons.Default.Person, "Profile") }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedButton(
                    onClick = { navController.navigate(AppRoutes.MY_CART) },
                    modifier = Modifier.weight(1f),
                ) {
                    Icon(Icons.Default.ShoppingCart, "Add to Cart")
                    Spacer(modifier = Modifier.size(8.dp))
                    Text("Add to Cart")
                }
                Button(
                    onClick = { navController.navigate(AppRoutes.SORGHUM_CHECKOUT) },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1FA74A))
                ) {
                    Text("Buy Now")
                }
            }
        },
        containerColor = Color(0xFFF8F9FA)
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            Image(
                painter = painterResource(id = R.drawable.sorghum_jpg),
                contentDescription = "Sorghum (Jowar)",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                contentScale = ContentScale.Crop
            )
            Column(modifier = Modifier.padding(16.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Top) {
                    Text("Sorghum (Jowar)", fontWeight = FontWeight.Bold, fontSize = 24.sp)
                    Text("₹42/kg", fontWeight = FontWeight.Bold, fontSize = 24.sp, color = Color(0xFF1FA74A))
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text("Sold by Mahila Shakti SHG", color = Color.Gray)
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Star, contentDescription = "Rating", tint = Color(0xFFFFC107), modifier = Modifier.size(16.dp))
                    Text("4.6 / 5", fontSize = 14.sp)
                }
                Spacer(modifier = Modifier.height(16.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Check, contentDescription = "Grade A Quality", tint = Color(0xFF1FA74A), modifier = Modifier.size(16.dp))
                    Text("Grade A Quality", color = Color(0xFF1FA74A), fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.weight(1f))
                    TextButton(onClick = { navController.navigate(AppRoutes.SORGHUM_TRACEABILITY) }) {
                        Text("View Traceability")
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text("Description", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text("Nutritious Jowar, rich in protein and fiber.", color = Color.Gray)
                Spacer(modifier = Modifier.height(16.dp))
                Text("Quantity (kg)", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = { if (quantity > 1) quantity-- }) { Icon(Icons.Default.Remove, "-") }
                    Text(quantity.toString(), fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    IconButton(onClick = { quantity++ }) { Icon(Icons.Default.Add, "+") }
                }
            }
        }
    }
}
