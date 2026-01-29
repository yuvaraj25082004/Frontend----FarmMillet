package com.simats.farmmillet

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material.icons.outlined.Payments
import androidx.compose.material.icons.outlined.ShoppingBasket
import androidx.compose.material.icons.outlined.Store
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun BottomNavigationBar(navController: NavController) {
    NavigationBar(
        modifier = Modifier.height(80.dp),
        containerColor = Color.White,
        tonalElevation = 8.dp
    ) {
        val greenColor = Color(0xFF1FA74A)
        val grayColor = Color.Gray

        val itemColors = NavigationBarItemDefaults.colors(
            selectedIconColor = greenColor,
            selectedTextColor = greenColor,
            indicatorColor = greenColor.copy(alpha = 0.1f),
            unselectedIconColor = grayColor,
            unselectedTextColor = grayColor
        )

        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, contentDescription = "Home", modifier = Modifier.size(26.dp)) },
            label = { Text("Home", fontSize = 12.sp, softWrap = false) },
            selected = navController.currentDestination?.route == AppRoutes.FARMER_DASHBOARD,
            onClick = { navController.navigate(AppRoutes.FARMER_DASHBOARD) },
            colors = itemColors
        )
        NavigationBarItem(
            icon = { Icon(Icons.Outlined.Inventory2, contentDescription = "Supply", modifier = Modifier.size(26.dp)) },
            label = { Text("Supply", fontSize = 12.sp, softWrap = false) },
            selected = navController.currentDestination?.route == AppRoutes.MY_SUPPLY_LIST,
            onClick = { navController.navigate(AppRoutes.MY_SUPPLY_LIST) },
            colors = itemColors
        )


        NavigationBarItem(
            icon = { Icon(Icons.Outlined.Payments, contentDescription = "Payment", modifier = Modifier.size(26.dp)) },
            label = { Text("Payment", fontSize = 12.sp, softWrap = false) },
            selected = navController.currentDestination?.route == AppRoutes.PAYMENT_HISTORY,
            onClick = { navController.navigate(AppRoutes.PAYMENT_HISTORY) },
            colors = itemColors
        )

        NavigationBarItem(
            icon = { Icon(Icons.Outlined.AccountCircle, contentDescription = "Profile", modifier = Modifier.size(26.dp)) },
            label = { Text("Profile", fontSize = 12.sp, softWrap = false) },
            selected = navController.currentDestination?.route == AppRoutes.USER_PROFILE,
            onClick = { navController.navigate(AppRoutes.USER_PROFILE) },
            colors = itemColors
        )
    }
}

@Composable
fun StatusChip(text: String, backgroundColor: Color, textColor: Color) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = backgroundColor,
    ) {
        Text(
            text = text,
            color = textColor,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
        )
    }
}