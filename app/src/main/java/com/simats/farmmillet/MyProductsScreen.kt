package com.simats.farmmillet

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

data class Product(val name: String, val quantity: String, val price: String, val grade: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyProductsScreen(navController: NavController) {
    var products by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf<List<ProductResponse>>(emptyList()) }
    var isLoading by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf(true) }
    var errorMessage by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf<String?>(null) }

    androidx.compose.runtime.LaunchedEffect(Unit) {
        try {
            val response = RetrofitClient.instance.getMyShgProducts()
            if (response.isSuccessful && response.body()?.success == true) {
                products = response.body()?.data ?: emptyList()
            } else {
                errorMessage = response.body()?.message ?: "Failed to fetch products"
            }
        } catch (e: Exception) {
            errorMessage = "Error: ${e.message}"
        } finally {
            isLoading = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Products", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { navController.navigate(AppRoutes.NOTIFICATIONS) }) {
                        Icon(Icons.Default.Notifications, contentDescription = "Notifications")
                    }
                    IconButton(onClick = { navController.navigate(AppRoutes.SHG_USER_PROFILE) }) {
                        Icon(Icons.Default.Person, contentDescription = "Profile")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(AppRoutes.ADD_PRODUCT_LISTING) },
                containerColor = Color(0xFF1FA74A)
            ) {
                Row(modifier = Modifier.padding(horizontal = 16.dp)) {
                    Icon(Icons.Default.Add, contentDescription = "Add New Product", tint = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Add New Product", color = Color.White)
                }
            }
        },
        bottomBar = { ShgBottomNavigationBar(navController) },
        containerColor = Color(0xFFF8F9FA)
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center), color = Color(0xFF1FA74A))
            } else if (errorMessage != null) {
                val error = errorMessage ?: "Unknown error"
                Text(error, modifier = Modifier.align(Alignment.Center), color = Color.Red)
            } else if (products.isEmpty()) {
                Text("No products listed yet", modifier = Modifier.align(Alignment.Center), color = Color.Gray)
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(products) { product ->
                        ProductItemCard(product)
                    }
                }
            }
        }
    }
}

@Composable
private fun ProductItemCard(product: ProductResponse) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.Top) {
                Text(product.milletType, fontWeight = FontWeight.SemiBold, fontSize = 18.sp, modifier = Modifier.weight(1f))
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFFE8F5E9),
                ) {
                    Text("Listed", color = Color(0xFF1FA74A), modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp), fontSize = 12.sp)
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            ProductDetailRow("Quantity:", "${product.quantityKg} kg")
            ProductDetailRow("Price:", "₹${product.pricePerKg}")
            ProductDetailRow("Grade:", product.qualityGrade)
            if (product.traceabilityId != null) {
                ProductDetailRow("Traceability:", product.traceabilityId)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                IconButton(onClick = { /* TODO: Edit */ }) {
                    Icon(Icons.Default.Edit, contentDescription = "Edit", tint = MaterialTheme.colorScheme.primary)
                }
                IconButton(onClick = { /* TODO: Delete */ }) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Red)
                }
            }
        }
    }
}

@Composable
private fun ProductDetailRow(label: String, value: String) {
    Row {
        Text(label, color = Color.Gray, fontSize = 14.sp, modifier = Modifier.width(80.dp))
        Text(value, fontWeight = FontWeight.Medium, fontSize = 14.sp)
    }
}

@Composable
private fun ShgBottomNavigationBar(navController: NavController) {
    NavigationBar(containerColor = Color.White, tonalElevation = 8.dp) {
        NavigationBarItem(icon = { Icon(Icons.Default.Home, "Home") }, label = { Text("Home") }, selected = false, onClick = { navController.navigate(AppRoutes.SHG_FPO_DASHBOARD) })
        NavigationBarItem(icon = { Icon(Icons.Default.Inventory, "Products") }, label = { Text("Products") }, selected = true, onClick = {  })
        NavigationBarItem(icon = { Icon(Icons.Default.ShoppingCart, "Orders") }, label = { Text("Orders") }, selected = false, onClick = { navController.navigate(AppRoutes.ORDERS_RECEIVED) })
        NavigationBarItem(icon = { Icon(Icons.Default.Payments, "Payments") }, label = { Text("Payments") }, selected = false, onClick = { navController.navigate(AppRoutes.SHG_PAYMENT_HISTORY) })
        NavigationBarItem(icon = { Icon(Icons.Default.Person, "Profile") }, label = { Text("Profile") }, selected = false, onClick = { navController.navigate(AppRoutes.SHG_USER_PROFILE) })
    }
}