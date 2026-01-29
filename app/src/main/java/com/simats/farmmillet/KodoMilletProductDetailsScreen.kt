package com.simats.farmmillet


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KodoMilletProductDetailsScreen(navController: NavController) {
    var quantity by remember { mutableStateOf(1) }
    val pricePerKg = 58

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Product Details") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { navController.navigate(AppRoutes.MY_CART) }) {
                        BadgedBox(badge = { Badge { Text("15") } }) {
                            Icon(imageVector = Icons.Default.ShoppingCart, contentDescription = "Cart")
                        }
                    }
                    IconButton(onClick = { navController.navigate(AppRoutes.NOTIFICATIONS) }) {
                        Icon(imageVector = Icons.Default.Notifications, contentDescription = "Notifications")
                    }
                    IconButton(onClick = { navController.navigate(AppRoutes.USER_PROFILE) }) {
                        Icon(imageVector = Icons.Default.Person, contentDescription = "Profile")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.kodo_millet_varagu),
                contentDescription = "Kodo Millet",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(16.dp))
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text("Kodo Millet", fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Text("(Varagu/Kodon)", fontSize = 18.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Sold by Mahila Shakti SHG", fontSize = 16.sp)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Default.Star, contentDescription = "Rating", tint = Color(0xFFFFC107))
                Text("4.4 / 5", fontSize = 16.sp)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Default.CheckCircle, contentDescription = "Grade B Quality", tint = Color(0xFF4CAF50))
                Text("Grade B Quality", fontSize = 16.sp, modifier = Modifier.padding(start = 8.dp))
                Spacer(modifier = Modifier.weight(1f))
                TextButton(onClick = { navController.navigate(AppRoutes.KODO_MILLET_TRACEABILITY) }) {
                    Text("View Traceability", color = MaterialTheme.colorScheme.primary)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text("Description", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Text("Kodo Millet - Great for diabetic-friendly diets.", fontSize = 16.sp)
            Spacer(modifier = Modifier.height(16.dp))
            Text("Quantity (kg)", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { if (quantity > 1) quantity-- }) {
                    Icon(imageVector = Icons.Default.Remove, contentDescription = "Decrease quantity")
                }
                Text(quantity.toString(), fontSize = 20.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 16.dp))
                IconButton(onClick = { quantity++ }) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Increase quantity")
                }
                Spacer(modifier = Modifier.weight(1f))
                Text("65kg available", color = Color.Gray)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Card(shape = RoundedCornerShape(16.dp), modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Total Price", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    Text("₹${quantity * pricePerKg}.00", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                Button(
                    onClick = { navController.navigate(AppRoutes.MY_CART) },
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = MaterialTheme.colorScheme.primary),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Add to Cart")
                }
                Button(
                    onClick = { navController.navigate(AppRoutes.KODO_MILLET_CHECKOUT) },
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 8.dp),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Buy Now")
                }
            }
        }
    }
}
