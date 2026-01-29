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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

private data class MilletCategory(val name: String, val scientificName: String, val benefits: List<String>, val imageRes: Int)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MilletCategoriesScreen(navController: NavController) {
    val categories = listOf(
        MilletCategory("Ragi", "Eleusine coracana", listOf("Rich in calcium", "Controls blood sugar"), R.drawable.finger_millet_ragi),
        MilletCategory("Bajra", "Pennisetum glaucum", listOf("Heart healthy", "Rich in magnesium"), R.drawable.pearl_millet_bajra),
        MilletCategory("Foxtail", "Setaria italica", listOf("Balances blood sugar", "Strengthens immunity"), R.drawable.foxtail_millet_thinai),
        MilletCategory("Jowar", "Sorghum bicolor", listOf("Gluten-free", "Rich in antioxidants"), R.drawable.sorghum_jpg),
        MilletCategory("Samai", "Panicum sumatrense", listOf("Low glycemic index", "Rich in B vitamins"), R.drawable.little_millet_samai),
        MilletCategory("Varagu", "Paspalum scrobiculatum", listOf("Controls diabetes", "Rich in antioxidants"), R.drawable.kodo_millet_varagu),
        MilletCategory("Cheena", "Panicum miliaceum", listOf("Easy to digest", "Cooling effect"), R.drawable.proso_millet),
        MilletCategory("Kuthiravali", "Echinochloa frumentacea", listOf("Low in calories", "High in fiber"), R.drawable.barnyard_millet)
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Millet Categories", fontWeight = FontWeight.Bold) },
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
        LazyColumn(
            contentPadding = padding,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1FA74A))
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text("Discover Millets", style = androidx.compose.material3.MaterialTheme.typography.headlineSmall, color = Color.White, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Explore the nutritional powerhouses that have sustained civilizations for thousands of years.", color = Color.White, style = androidx.compose.material3.MaterialTheme.typography.bodyMedium)
                    }
                }
            }
            items(categories) { category ->
                CategoryCard(category)
            }
        }
    }
}

@Composable
private fun CategoryCard(category: MilletCategory) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = category.imageRes),
                contentDescription = category.name,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.padding(8.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(category.name, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text(category.scientificName, color = Color.Gray, fontSize = 12.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    category.benefits.forEach {
                        BenefitChip(it)
                    }
                }
            }
            Icon(Icons.AutoMirrored.Filled.ArrowForwardIos, "Details", modifier = Modifier.size(16.dp), tint = Color.Gray)
        }
    }
}

@Composable
private fun BenefitChip(text: String) {
    Box(
        modifier = Modifier
            .background(Color(0xFFE8F5E9), RoundedCornerShape(8.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(text, color = Color(0xFF1B5E20), fontSize = 10.sp)
    }
}
