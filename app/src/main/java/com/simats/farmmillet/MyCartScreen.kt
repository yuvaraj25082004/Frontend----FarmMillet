package com.simats.farmmillet

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
fun MyCartScreen(navController: NavController) {
    val cartItems = CartManager.items

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Cart", fontWeight = FontWeight.Bold) },
                navigationIcon = { IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back") } },
                actions = {
                    IconButton(onClick = { navController.navigate(AppRoutes.CONSUMER_NOTIFICATIONS) }) { Icon(Icons.Default.Notifications, "Notifications") }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        bottomBar = {
            if (cartItems.isNotEmpty()) {
                PriceSummary(items = cartItems, navController = navController)
            }
        },
        containerColor = Color(0xFFF8F9FA)
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            if (cartItems.isEmpty()) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(Icons.Default.ShoppingCart, "Empty Cart", modifier = Modifier.size(64.dp), tint = Color.LightGray)
                    Spacer(Modifier.height(16.dp))
                    Text("Your cart is empty", color = Color.Gray, fontSize = 18.sp)
                    Spacer(Modifier.height(24.dp))
                    Button(onClick = { navController.navigate(AppRoutes.CONSUMER_MARKETPLACE) }, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1FA74A))) {
                        Text("Explore Marketplace")
                    }
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.padding(16.dp)
                ) {
                    items(cartItems) { item ->
                        CartListItem(
                            item = item,
                            onDelete = { CartManager.removeItem(item) },
                            onUpdateQuantity = { CartManager.updateQuantity(item, it) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CartListItem(item: CartItem, onDelete: () -> Unit, onUpdateQuantity: (Int) -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = item.imageRes),
                contentDescription = item.name,
                modifier = Modifier.size(80.dp).background(Color(0xFFF0F0F0), RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.padding(8.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(item.name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text("by ${item.shgName}", color = Color.Gray, fontSize = 12.sp)
                Text("₹${item.price}/kg", fontWeight = FontWeight.SemiBold, color = Color(0xFF1FA74A), fontSize = 14.sp)
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { onUpdateQuantity(item.quantity - 1) }) { Icon(Icons.Default.Remove, "-") }
                Text(item.quantity.toString(), fontWeight = FontWeight.Bold, fontSize = 16.sp)
                IconButton(onClick = { onUpdateQuantity(item.quantity + 1) }) { Icon(Icons.Default.Add, "+") }
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, "Delete", tint = Color.Red.copy(alpha = 0.7f))
            }
        }
    }
}

@Composable
private fun PriceSummary(items: List<CartItem>, navController: NavController) {
    val subtotal = items.sumOf { it.price * it.quantity }
    val deliveryFee = 20.00
    val total = subtotal + deliveryFee

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(16.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Subtotal ( ${items.size} items )", color = Color.Gray)
            Text("₹%.2f".format(subtotal), fontWeight = FontWeight.SemiBold)
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Delivery Fee", color = Color.Gray)
            Text("₹%.2f".format(deliveryFee), fontWeight = FontWeight.SemiBold)
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Total Amount", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Text("₹%.2f".format(total), fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color(0xFF1FA74A))
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { navController.navigate(AppRoutes.CHECKOUT) },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1FA74A)),
            contentPadding = PaddingValues(vertical = 12.dp)
        ) {
            Text("Proceed to Checkout", fontSize = 16.sp)
        }
    }
}
