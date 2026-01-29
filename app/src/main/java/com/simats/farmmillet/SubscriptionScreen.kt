package com.simats.farmmillet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun SubscriptionScreen(navController: NavController) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0A0E27))
            .verticalScroll(scrollState)
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(40.dp))

        // 🌾 App Icon / Logo Placeholder
        Text(
            text = "🌾",
            fontSize = 64.sp
        )

        Text(
            text = "FarmMillet Premium",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )

        Text(
            text = "Millets Value Chain Platform",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFFFD700),
            modifier = Modifier.padding(top = 6.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Connect farmers, SHGs/FPOs, and consumers through a trusted farm-to-fork digital millet ecosystem.",
            color = Color(0xFFB8C5D6),
            fontSize = 16.sp,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        SubscriptionFeatureCard(
            icon = "🚜",
            title = "Direct Farmer & SHG Access",
            subtitle = "Fair pricing without intermediaries"
        )

        SubscriptionFeatureCard(
            icon = "🔍",
            title = "Farm-to-Fork Traceability",
            subtitle = "Origin, quality & authenticity visibility"
        )

        SubscriptionFeatureCard(
            icon = "🛒",
            title = "Integrated Millet Marketplace",
            subtitle = "E-commerce, logistics & digital payments"
        )

        Spacer(modifier = Modifier.height(32.dp))

        // 💰 Pricing
        Card(
            colors = CardDefaults.cardColors(containerColor = Color(0xFF6C5CE7)),
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "₹100",
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "/ month",
                    color = Color(0xFFE0E7FF)
                )
                Text(
                    text = "Cancel anytime",
                    fontSize = 12.sp,
                    color = Color(0xFFB0B9DD),
                    modifier = Modifier.padding(top = 6.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // ⭐ Subscribe Button
        Button(
            onClick = {
                // TODO: Implement subscription flow
                // For now, navigate to consumer dashboard
                navController.navigate(AppRoutes.CONSUMER_DASHBOARD) {
                    popUpTo(AppRoutes.CONSUMER_REGISTER) { inclusive = true }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp),
            shape = RoundedCornerShape(32.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.White)
        ) {
            Text(
                text = "Start Premium",
                color = Color.Black,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "By continuing, you agree to our Terms & Privacy Policy",
            fontSize = 12.sp,
            color = Color(0xFF7C8AA8),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(40.dp))

        TextButton(onClick = {
            // Navigate to consumer login
            navController.navigate(AppRoutes.CONSUMER_LOGIN) {
                popUpTo(AppRoutes.CONSUMER_REGISTER) { inclusive = true }
            }
        }) {
            Text(
                text = "Maybe later",
                color = Color(0xFF7C8AA8)
            )
        }

        Spacer(modifier = Modifier.height(40.dp))
    }
}

@Composable
fun SubscriptionFeatureCard(
    icon: String,
    title: String,
    subtitle: String
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1F3A)),
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp)
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = icon,
                fontSize = 28.sp
            )

            Column(
                modifier = Modifier
                    .padding(start = 16.dp)
                    .weight(1f)
            ) {
                Text(
                    text = title,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = subtitle,
                    color = Color(0xFF7C8AA8),
                    fontSize = 14.sp
                )
            }

            Text(
                text = "✓",
                color = Color(0xFF4CAF50),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
