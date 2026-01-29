package com.simats.farmmillet

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoleSelectionScreen(navController: NavController) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Who are you?") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Text(
                text = "Select your role to continue",
                color = Color.Gray
            )

            RoleCard(
                title = "Farmer",
                subtitle = "I grow and sell millets",
                icon = Icons.Filled.Spa,
                bgColor = Color(0xFFDFF6E5),
                onClick = { navController.navigate(AppRoutes.FARMER_LOGIN) }
            )

            RoleCard(
                title = "SHG / FPO",
                subtitle = "I aggregate and sell millet products",
                icon = Icons.Filled.Groups,
                bgColor = Color(0xFFE4EEFF),
                onClick = { navController.navigate(AppRoutes.SHG_FPO_LOGIN) }
            )

            RoleCard(
                title = "Consumer",
                subtitle = "I want to buy millet products",
                icon = Icons.Default.ShoppingCart,
                bgColor = Color(0xFFFFEFD9),
                onClick = { navController.navigate(AppRoutes.CONSUMER_LOGIN) }
            )
        }
    }
}

@Composable
fun RoleCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    bgColor: Color,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Surface(
                shape = CircleShape,
                color = bgColor,
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    icon,
                    contentDescription = title,
                    modifier = Modifier.padding(12.dp)
                )
            }

            Spacer(Modifier.width(16.dp))

            Column {
                Text(title, style = MaterialTheme.typography.titleMedium)
                Text(subtitle, color = Color.Gray)
            }
        }
    }
}