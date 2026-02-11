package com.simats.farmmillet

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

private data class ConsumerDashboardProduct(val name: String, val shg: String, val price: String, val rating: String, val imageRes: Int)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConsumerDashboardScreen(navController: NavController) {
    var products by remember { mutableStateOf<List<ProductResponse>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        try {
            val response = RetrofitClient.instance.getProducts()
            if (response.isSuccessful && response.body()?.success == true) {
                products = response.body()?.data?.take(2) ?: emptyList()
            }
        } catch (e: Exception) {
            // Silently fail or show minimal error for dashboard
        } finally {
            isLoading = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = R.drawable.farmmillet_logo),
                            contentDescription = "logo",
                            modifier = Modifier.size(36.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text("FarmMillet", fontWeight = FontWeight.Bold)
                    }
                },
                actions = {
                    IconButton(onClick = { navController.navigate(AppRoutes.CONSUMER_NOTIFICATIONS) }) {
                        Icon(Icons.Default.Notifications, contentDescription = "Notifications")
                    }
                    IconButton(onClick = { navController.navigate(AppRoutes.CONSUMER_USER_PROFILE) }) {
                        Icon(Icons.Outlined.Person, contentDescription = "Profile")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        },
        bottomBar = { ConsumerBottomNavigationBar(navController) },
        containerColor = Color(0xFFF8F9FA)
    ) { padding ->
        Column(modifier = Modifier.padding(padding).verticalScroll(rememberScrollState())) {
            HeaderCard(navController)
            Spacer(modifier = Modifier.height(24.dp))
            FeaturedProductsSection(navController, products, isLoading)
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun HeaderCard(navController: NavController) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val userName: String = androidx.compose.runtime.remember { TokenManager.getUserName(context) }
    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1FA74A)),
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            val welcomeName = if (userName.isEmpty()) "User" else userName
            Text("Hello, $welcomeName", style = MaterialTheme.typography.headlineSmall, color = Color.White, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Discover healthy & organic Millets1 directly from farmers.", color = Color.White, style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { navController.navigate(AppRoutes.CONSUMER_MARKETPLACE) }, colors = ButtonDefaults.buttonColors(containerColor = Color.White), shape = RoundedCornerShape(8.dp)) {
                Text("Shop Now", color = Color(0xFF1FA74A), fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Composable
private fun FeaturedProductsSection(navController: NavController, products: List<ProductResponse>, isLoading: Boolean) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Featured Products", style = MaterialTheme.typography.titleLarge, modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
            Text("See All >", color = Color(0xFF1FA74A), fontWeight = FontWeight.SemiBold, modifier = Modifier.clickable { navController.navigate(AppRoutes.CONSUMER_MARKETPLACE) })
        }
        Spacer(modifier = Modifier.height(16.dp))
        if (isLoading) {
            CircularProgressIndicator(color = Color(0xFF1FA74A), modifier = Modifier.align(Alignment.CenterHorizontally))
        } else if (products.isEmpty()) {
            Text("No products available", color = Color.Gray)
        } else {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                products.forEach { product ->
                    Box(modifier = Modifier.weight(1f)) {
                        ProductCard(product, onClick = { navController.navigate("product_details/${product.id}") })
                    }
                }
            }
        }
    }
}

@Composable
private fun ProductCard(product: ProductResponse, onClick: () -> Unit) {
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

    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier.clickable { onClick() }
    ) {
        Column {
            Box(modifier = Modifier.fillMaxWidth().height(120.dp)) {
                Image(
                    painter = painterResource(id = imageRes),
                    contentDescription = product.milletType,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                Box(
                    modifier = Modifier
                        .padding(8.dp)
                        .background(Color.White.copy(alpha = 0.7f), CircleShape)
                        .padding(6.dp)
                        .align(Alignment.TopEnd)
                ) {
                    Icon(imageVector = Icons.Outlined.FavoriteBorder, contentDescription = "Save", tint = Color.Black, modifier = Modifier.size(16.dp))
                }
            }
            Column(modifier = Modifier.padding(12.dp)) {
                Text(product.milletType, fontWeight = FontWeight.Bold, fontSize = 16.sp, maxLines = 1)
                Text(product.shgName ?: "Local SHG", color = Color.Gray, fontSize = 12.sp, maxLines = 1)
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("₹${product.pricePerKg.toInt()}/kg", fontWeight = FontWeight.Bold, color = Color(0xFF1FA74A), fontSize = 18.sp, modifier = Modifier.weight(1f))
                    Surface(shape = RoundedCornerShape(4.dp), color = Color(0xFFC8E6C9)) {
                        Row(modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp), verticalAlignment = Alignment.CenterVertically) {
                            Text("4.5", color = Color(0xFF1FA74A), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.width(2.dp))
                            Icon(Icons.Default.Star, contentDescription = "Rating", tint = Color(0xFF1FA74A), modifier = Modifier.size(14.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ConsumerBottomNavigationBar(navController: NavController) {
    NavigationBar(containerColor = Color.White, tonalElevation = 8.dp) {
        NavigationBarItem(icon = { Icon(Icons.Filled.Home, "Home") }, label = { Text("Home") }, selected = navController.currentDestination?.route == AppRoutes.CONSUMER_DASHBOARD, onClick = { navController.navigate(AppRoutes.CONSUMER_DASHBOARD) })
        NavigationBarItem(icon = { Icon(Icons.Outlined.ShoppingBag, "Products") }, label = { Text("Products") }, selected =  navController.currentDestination?.route == AppRoutes.MY_ORDERS, onClick = { navController.navigate(AppRoutes.CONSUMER_MARKETPLACE) })
        NavigationBarItem(icon = { Icon(painterResource(id = R.drawable.ic_orders), "Orders") }, label = { Text("Orders") }, selected = navController.currentDestination?.route == AppRoutes.MY_ORDERS, onClick = { navController.navigate(AppRoutes.MY_ORDERS) })
        NavigationBarItem(icon = { Icon(Icons.Default.Person, "Profile") }, label = { Text("Profile") }, selected = false, onClick = { navController.navigate(AppRoutes.CONSUMER_USER_PROFILE) })


    }
}
