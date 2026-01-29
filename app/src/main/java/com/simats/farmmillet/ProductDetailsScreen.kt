package com.simats.farmmillet

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
fun ProductDetailsScreen(navController: NavController, productId: String?) {
    var quantity by remember { mutableStateOf(1) }
    var product by remember { mutableStateOf<ProductResponse?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(productId) {
        if (productId == null) {
            errorMessage = "Invalid Product ID"
            isLoading = false
            return@LaunchedEffect
        }
        try {
            val response = RetrofitClient.instance.getProduct(productId.toInt())
            if (response.isSuccessful && response.body()?.success == true) {
                product = response.body()?.data?.get("product")
            } else {
                errorMessage = response.body()?.message ?: "Failed to fetch product"
            }
        } catch (e: Exception) {
            errorMessage = "Error: ${e.message}"
        } finally {
            isLoading = false
        }
    }

    val currentProduct = product
    val imageRes = if (currentProduct != null) {
        when {
            currentProduct.milletType.contains("Bajra", true) -> R.drawable.pearl_millet_bajra
            currentProduct.milletType.contains("Ragi", true) -> R.drawable.finger_millet_ragi
            currentProduct.milletType.contains("Foxtail", true) -> R.drawable.foxtail_millet_thinai
            currentProduct.milletType.contains("Sorghum", true) -> R.drawable.sorghum_jpg
            currentProduct.milletType.contains("Little", true) -> R.drawable.little_millet_samai
            currentProduct.milletType.contains("Kodo", true) -> R.drawable.kodo_millet_varagu
            currentProduct.milletType.contains("Proso", true) -> R.drawable.proso_millet
            currentProduct.milletType.contains("Barnyard", true) -> R.drawable.barnyard_millet
            else -> R.drawable.finger_millet_ragi // Default
        }
    } else R.drawable.finger_millet_ragi

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(currentProduct?.milletType ?: "Product Details", fontWeight = FontWeight.Bold) },
                navigationIcon = { IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back") } },
                actions = {
                    IconButton(onClick = { navController.navigate(AppRoutes.MY_CART) }) {
                        Icon(Icons.Default.ShoppingCart, contentDescription = "Cart")
                    }
                    IconButton(onClick = { navController.navigate(AppRoutes.CONSUMER_NOTIFICATIONS) }) { Icon(Icons.Default.Notifications, "Notifications") }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        bottomBar = {
            if (currentProduct != null) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    val context = androidx.compose.ui.platform.LocalContext.current
                    OutlinedButton(
                        onClick = { 
                            CartManager.addToCart(currentProduct, quantity, imageRes)
                            android.widget.Toast.makeText(context, "${currentProduct.milletType} added to cart!", android.widget.Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier.weight(1f),
                    ) {
                        Icon(Icons.Default.ShoppingCart, "Add to Cart")
                        Spacer(modifier = Modifier.size(8.dp))
                        Text("Add to Cart")
                    }
                    Button(
                        onClick = { navController.navigate(AppRoutes.CHECKOUT) },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1FA74A))
                    ) {
                        Text("Buy Now")
                    }
                }
            }
        },
        containerColor = Color(0xFFF8F9FA)
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center), color = Color(0xFF1FA74A))
            } else if (errorMessage != null) {
                Text(errorMessage!!, modifier = Modifier.align(Alignment.Center), color = Color.Red)
            } else if (currentProduct != null) {
                Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                    Image(
                        painter = painterResource(id = imageRes),
                        contentDescription = currentProduct.milletType,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp),
                        contentScale = ContentScale.Crop
                    )
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Top) {
                            Text(currentProduct.milletType, fontWeight = FontWeight.Bold, fontSize = 24.sp)
                            Text("₹${currentProduct.pricePerKg}/kg", fontWeight = FontWeight.Bold, fontSize = 24.sp, color = Color(0xFF1FA74A))
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        currentProduct.shgName?.let { Text("Sold by $it", color = Color.Gray) }
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Star, contentDescription = "Rating", tint = Color(0xFFFFC107), modifier = Modifier.size(16.dp))
                            Text("4.5 / 5", fontSize = 14.sp)
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Check, contentDescription = "Grade ${currentProduct.qualityGrade} Quality", tint = Color(0xFF1FA74A), modifier = Modifier.size(16.dp))
                            Text("Grade ${currentProduct.qualityGrade} Quality", color = Color(0xFF1FA74A), fontWeight = FontWeight.SemiBold)
                            Spacer(modifier = Modifier.weight(1f))
                            if (currentProduct.traceabilityId != null) {
                                TextButton(onClick = { navController.navigate("traceability_journey/${currentProduct.traceabilityId}") }) {
                                    Text("View Traceability")
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Description", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(currentProduct.description ?: "No description available.", color = Color.Gray)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Quantity (kg)", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(onClick = { if (quantity > 1) quantity-- }) { Icon(Icons.Default.Remove, "-") }
                            Text(quantity.toString(), fontWeight = FontWeight.Bold, fontSize = 20.sp, modifier = Modifier.padding(horizontal = 16.dp))
                            IconButton(onClick = { if (quantity < (currentProduct.quantityKg ?: 0.0)) quantity++ }) { Icon(Icons.Default.Add, "+") }
                        }
                        Spacer(modifier = Modifier.height(24.dp))
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(2.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text("Total Price", style = MaterialTheme.typography.titleMedium, color = Color.Gray)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("₹${quantity * currentProduct.pricePerKg}.00", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
    }
}
