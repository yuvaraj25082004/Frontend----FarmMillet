package com.simats.farmmillet


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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingDown
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FarmerDashboardScreen(navController: NavController) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var stats by remember { mutableStateOf<FarmerStats?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        try {
            val response = RetrofitClient.instance.getFarmerDashboard()
            if (response.isSuccessful && response.body()?.success == true) {
                stats = response.body()?.data?.summary
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
                title = { Text("FarmMillet", fontWeight = FontWeight.Bold) },
                actions = {
                    IconButton(onClick = { navController.navigate(AppRoutes.NOTIFICATIONS) }) {
                        Icon(Icons.Default.Notifications, contentDescription = "Notifications")
                    }
                    IconButton(onClick = { navController.navigate(AppRoutes.USER_PROFILE) }) {
                        Icon(Icons.Default.Person, contentDescription = "Profile")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        },
        bottomBar = { BottomNavigationBar(navController) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF9F9F9))
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 24.dp)

        ) {
            val userName: String = remember { TokenManager.getUserName(context) }
            Text("Welcome, ${userName.ifEmpty { "Farmer" }}!", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2E7D32))
            Spacer(modifier = Modifier.height(16.dp))
            
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally), color = Color(0xFF2E7D32))
            } else {
                SummarySection(stats)
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            QuickActionsSection(navController)

            Spacer(modifier = Modifier.height(24.dp))
            SalesSummaryCard(navController)
        }
    }
}

@Composable
fun SummarySection(stats: FarmerStats?) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        SummaryCard(stats?.totalQuantity?.toInt()?.toString() ?: "0", "Supply (kg)", Color(0xFFF3E5F5))
        SummaryCard(stats?.pendingCount?.toString() ?: "0", "Pending", Color(0xFFE3F2FD))
        SummaryCard("₹${stats?.totalEarnings?.toInt() ?: 0}", "Earnings", Color(0xFFFFF3E0))
    }
}

@Composable
fun SummaryCard(value: String, label: String, backgroundColor: Color) {
    Card(
        modifier = Modifier.size(110.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(value, fontWeight = FontWeight.Bold, fontSize = 24.sp)
            Text(label, fontSize = 14.sp, color = Color.Gray)
        }
    }
}

@Composable
fun QuickActionsSection(navController: NavController) {
    Column {
        Text("Quick Actions", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            QuickActionCard("Add Supply", Icons.Default.Add, Color(0xFF1FA74A), Color.White, Modifier.weight(1f)) { navController.navigate(AppRoutes.ADD_MILLET_SUPPLY) }
            QuickActionCard("View Supply", Icons.Default.Description, Color.White, Color.Black, Modifier.weight(1f)) { navController.navigate(AppRoutes.MY_SUPPLY_LIST) }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            QuickActionCard("Receipts", Icons.Default.AccountBalanceWallet, Color.White, Color.Black, Modifier.weight(1f)) { navController.navigate(AppRoutes.PAYMENT_HISTORY) }
        }
    }
}

@Composable
fun QuickActionCard(label: String, icon: ImageVector, backgroundColor: Color, tint: Color, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Card(
        modifier = modifier.height(120.dp).clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(icon, contentDescription = label, modifier = Modifier.size(36.dp), tint = tint)
            Spacer(modifier = Modifier.height(8.dp))
            Text(label, fontWeight = FontWeight.SemiBold, color = tint)
        }
    }
}



@Composable
fun SalesSummaryCard(navController: NavController) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable { navController.navigate(AppRoutes.SALES_SUMMARY) },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp).fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.PieChart, "Sales Summary", modifier = Modifier.size(36.dp))
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text("Sales Summary", fontWeight = FontWeight.Bold)
                Text("View detailed analytics", color = Color.Gray, fontSize = 14.sp)
            }
        }
    }
}
