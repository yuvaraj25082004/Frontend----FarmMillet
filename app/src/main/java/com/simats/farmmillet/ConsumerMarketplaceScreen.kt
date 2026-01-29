package com.simats.farmmillet

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.CircularProgressIndicator
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

import androidx.compose.runtime.*
import kotlinx.coroutines.launch

private data class MarketplaceProduct(val name: String, val shg: String, val price: String, val grade: String, val imageRes: Int, val available: String, val id: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConsumerMarketplaceScreen(navController: NavController) {
    var products by remember { mutableStateOf<List<ProductResponse>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var searchQuery by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        try {
            val response = RetrofitClient.instance.getProducts()
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
                title = { Text("Marketplace", fontWeight = FontWeight.Bold) },
                navigationIcon = { IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back") } },
                actions = {
                    IconButton(onClick = { navController.navigate(AppRoutes.MY_CART) }) { Icon(Icons.Default.ShoppingCart, "Cart") }
                    IconButton(onClick = { navController.navigate(AppRoutes.CONSUMER_NOTIFICATIONS) }) { Icon(Icons.Default.Notifications, "Notifications") }
                    IconButton(onClick = { navController.navigate(AppRoutes.CONSUMER_USER_PROFILE) }) { Icon(Icons.Default.Person, "Profile") }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        bottomBar = { ConsumerBottomNavigationBar(navController) },
        containerColor = Color(0xFFF8F9FA)
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    label = { Text("Search millets...") },
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(Icons.Default.FilterList, "Filter")
                }
            }
            
            Box(modifier = Modifier.fillMaxSize()) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center), color = Color(0xFF1FA74A))
                } else if (errorMessage != null) {
                    Text(errorMessage!!, modifier = Modifier.align(Alignment.Center), color = Color.Red)
                } else if (products.isEmpty()) {
                    Text("No products available", modifier = Modifier.align(Alignment.Center), color = Color.Gray)
                } else {
                    val filteredProducts = products.filter { it.milletType.contains(searchQuery, ignoreCase = true) }
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        contentPadding = PaddingValues(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(filteredProducts) { product ->
                            ProductCard(product, navController)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ProductCard(product: ProductResponse, navController: NavController) {
    val imageRes = when {
        product.milletType.contains("Bajra", true) -> R.drawable.pearl_millet_bajra
        product.milletType.contains("Ragi", true) -> R.drawable.finger_millet_ragi
        product.milletType.contains("Foxtail", true) -> R.drawable.foxtail_millet_thinai
        product.milletType.contains("Sorghum", true) -> R.drawable.sorghum_jpg
        product.milletType.contains("Little", true) -> R.drawable.little_millet_samai
        product.milletType.contains("Kodo", true) -> R.drawable.kodo_millet_varagu
        product.milletType.contains("Proso", true) -> R.drawable.proso_millet
        product.milletType.contains("Barnyard", true) -> R.drawable.barnyard_millet
        else -> R.drawable.finger_millet_ragi // Default
    }

    val route = "product_details/${product.id}"

    Card(
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier.clickable { navController.navigate(route) }
    ) {
        Column {
            Box(modifier = Modifier.height(120.dp)) {
                Image(
                    painter = painterResource(id = imageRes),
                    contentDescription = product.milletType,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                Box(
                    modifier = Modifier
                        .padding(8.dp)
                        .background(Color.Black.copy(alpha = 0.5f), RoundedCornerShape(4.dp))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text("Grade ${product.qualityGrade}", color = Color.White, fontSize = 10.sp)
                }
            }
            Column(modifier = Modifier.padding(12.dp)) {
                Text(product.milletType, fontWeight = FontWeight.Bold, fontSize = 14.sp, maxLines = 1)
                product.shgName?.let { Text(it, color = Color.Gray, fontSize = 12.sp, maxLines = 1) }
                Spacer(modifier = Modifier.height(8.dp))
                Text("₹${product.pricePerKg}/kg", fontWeight = FontWeight.SemiBold, color = Color(0xFF1FA74A), fontSize = 16.sp)
                Text("${product.quantityKg}kg avail.", color = Color.Gray, fontSize = 10.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = { navController.navigate(route) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1FA74A)),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    Text("View")
                }
            }
        }
    }
}
