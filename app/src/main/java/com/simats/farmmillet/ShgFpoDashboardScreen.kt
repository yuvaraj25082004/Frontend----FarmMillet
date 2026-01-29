package com.simats.farmmillet

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Verified
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShgFpoDashboardScreen(navController: NavController) {
    val context = LocalContext.current
    var dashboardData by remember { mutableStateOf<SHGDashboardResponse?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        try {
            val response = RetrofitClient.instance.getShgDashboard()
            if (response.isSuccessful && response.body()?.success == true) {
                dashboardData = response.body()?.data
            }
        } catch (e: Exception) {
            // Handle error
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
                            modifier = Modifier.size(36.dp).clip(CircleShape)
                        )
                        Spacer(Modifier.width(12.dp))
                        Text("FarmMillet", fontWeight = FontWeight.Bold)
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
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White),
            )
        },
        bottomBar = { ShgBottomNavigationBar(navController) },
        containerColor = Color(0xFFF8F9FA)
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            val userName: String = remember { TokenManager.getUserName(context) }
            Text("Welcome, ${userName.ifEmpty { "SHG/FPO" }}!", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1FA74A))
            Spacer(modifier = Modifier.height(16.dp))
            ManagementCard()
            Spacer(modifier = Modifier.height(24.dp))
            
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally), color = Color(0xFF1FA74A))
            } else {
                ShgSummarySection(dashboardData)
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            QuickActionsGrid(navController)
        }
    }
}

@Composable
private fun ManagementCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Text(
            text = "Manage your millet value chain efficiently",
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(20.dp)
        )
    }
}

@Composable
private fun ShgSummarySection(data: SHGDashboardResponse?) {
    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        SummaryMetric(data?.totalProducts?.toString() ?: "0", "Products", Color(0xFF4285F4), Modifier.weight(1f))
        SummaryMetric(data?.pendingOrders?.toString() ?: "0", "Orders", Color(0xFF4285F4), Modifier.weight(1f))
        SummaryMetric("₹${(data?.totalRevenue ?: 0.0) / 1000}k", "Revenue", Color(0xFF1FA74A), Modifier.weight(1f))
    }
}

@Composable
private fun SummaryMetric(value: String, label: String, valueColor: Color, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(value, fontWeight = FontWeight.Bold, fontSize = 20.sp, color = valueColor)
            Text(label, fontSize = 14.sp, color = Color.Gray)
        }
    }
}

@Composable
private fun QuickActionsGrid(navController: NavController) {
    Column {
        Text("Quick Actions", fontWeight = FontWeight.SemiBold, fontSize = 18.sp)
        Spacer(modifier = Modifier.height(16.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            QuickAction("Farmer Supplies", Icons.Default.People, Color(0xFFE8F5E9), Modifier.weight(1f)) { navController.navigate(AppRoutes.FARMER_SUPPLIES) }

            QuickAction("My Products", Icons.Default.Description, Color(0xFFF3E5F5), Modifier.weight(1f)) { navController.navigate(AppRoutes.MY_PRODUCTS) }
            QuickAction("Orders", Icons.Default.ShoppingCart, Color(0xFFFFF3E0), Modifier.weight(1f)) { navController.navigate(AppRoutes.ORDERS_RECEIVED) }
        }
        Spacer(modifier = Modifier.height(12.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {

            QuickAction("Payments", Icons.Default.Payments, Color(0xFFFFEBEE), Modifier.weight(1f)) { navController.navigate(AppRoutes.SHG_PAYMENT_HISTORY) }
            QuickAction("Traceability", Icons.Default.Verified, Color(0xFFE8EAF6), Modifier.weight(1f)) { navController.navigate(AppRoutes.TRACEABILITY_RECORDS) }
            QuickAction("Analytics", Icons.Default.Analytics, Color(0xFFF1F8E9), Modifier.weight(1f)) { navController.navigate(AppRoutes.SHG_ANALYTICS) }
        }
    }
}

@Composable
private fun QuickAction(title: String, icon: ImageVector, color: Color, modifier: Modifier, onClick: () -> Unit) {
    Card(
        modifier = modifier.clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = color),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(
            modifier = Modifier.padding(vertical = 20.dp, horizontal = 12.dp).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(icon, contentDescription = title, modifier = Modifier.size(32.dp), tint = Color.DarkGray)
            Spacer(modifier = Modifier.height(12.dp))
            Text(title, fontWeight = FontWeight.SemiBold, fontSize = 12.sp, maxLines = 1, textAlign = TextAlign.Center)
        }
    }
}

@Composable
private fun ShgBottomNavigationBar(navController: NavController) {
    NavigationBar(containerColor = Color.White, tonalElevation = 8.dp) {
        NavigationBarItem(icon = { Icon(Icons.Default.Home, "Home") }, label = { Text("Home") }, selected = true, onClick = { /* Current */ })
        NavigationBarItem(icon = { Icon(Icons.Default.Inventory, "Products") }, label = { Text("Products") }, selected = false, onClick = { navController.navigate(AppRoutes.MY_PRODUCTS) })
        NavigationBarItem(icon = { Icon(Icons.Default.ShoppingCart, "Orders") }, label = { Text("Orders") }, selected = false, onClick = { navController.navigate(AppRoutes.ORDERS_RECEIVED) })
        NavigationBarItem(icon = { Icon(Icons.Default.Payments, "Payments") }, label = { Text("Payments") }, selected = false, onClick = { navController.navigate(AppRoutes.SHG_PAYMENT_HISTORY) })
        NavigationBarItem(icon = { Icon(Icons.Default.Person, "Profile") }, label = { Text("Profile") }, selected = false, onClick = { navController.navigate(AppRoutes.SHG_USER_PROFILE) })
    }
}