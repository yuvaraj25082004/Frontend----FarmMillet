package com.simats.farmmillet

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.simats.farmmillet.ui.theme.FarmMilletTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Initial setup for production: Check for existing session
        val token = TokenManager.getToken(this)
        val role = TokenManager.getUserRole(this)
        
        // Set token in Retrofit early
        if (token != null) {
            RetrofitClient.setToken(token)
        }

        val startDest = when {
            token != null && role == "farmer" -> AppRoutes.FARMER_DASHBOARD
            token != null && role == "shg" -> AppRoutes.SHG_FPO_DASHBOARD
            token != null && role == "consumer" -> AppRoutes.CONSUMER_DASHBOARD
            else -> AppRoutes.WELCOME
        }

        setContent {
            FarmMilletTheme {
                AppNavigation(startDest)
            }
        }
    }
}

@Composable
fun AppNavigation(startDestination: String = AppRoutes.WELCOME) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = startDestination) {
        composable(AppRoutes.WELCOME) { WelcomeScreen(navController) }
        composable(AppRoutes.ROLE_SELECTION) { RoleSelectionScreen(navController) }

        // Farmer Flow
        composable(AppRoutes.FARMER_LOGIN) { FarmerLoginScreen(navController) }
        composable(AppRoutes.FARMER_RESET_PASSWORD) { FarmerResetPasswordScreen(navController) }
        composable(
            route = AppRoutes.FARMER_VERIFICATION_CODE,
            arguments = listOf(navArgument("email") { type = NavType.StringType })
        ) { backStackEntry ->
            FarmerVerificationCodeScreen(navController, backStackEntry.arguments?.getString("email"))
        }
        composable(AppRoutes.FARMER_SET_NEW_PASSWORD) { FarmerSetNewPasswordScreen(navController) }
        composable(AppRoutes.FARMER_DASHBOARD) { FarmerDashboardScreen(navController) }
        composable(AppRoutes.FARMER_PROFILE) { FarmerProfileScreen(navController) }
        composable(AppRoutes.FARMER_PASSWORD) { FarmerPasswordScreen(navController) }

        // SHG/FPO Flow
        composable(AppRoutes.SHG_FPO_LOGIN) { ShgFpoLoginScreen(navController) }
        composable(AppRoutes.SHG_FPO_RESET_PASSWORD) { ShgFpoResetPasswordScreen(navController) }
        composable(
            route = AppRoutes.SHG_FPO_VERIFICATION_CODE,
            arguments = listOf(navArgument("email") { type = NavType.StringType })
        ) { backStackEntry ->
            ShgFpoVerificationCodeScreen(navController, backStackEntry.arguments?.getString("email"))
        }
        composable(AppRoutes.SHG_FPO_SET_NEW_PASSWORD) { ShgFpoSetNewPasswordScreen(navController) }
        composable(AppRoutes.SHG_FPO_DASHBOARD) { ShgFpoDashboardScreen(navController) }
        composable(AppRoutes.SHG_FPO_PROFILE) { ShgFpoProfileScreen(navController) }
        composable(AppRoutes.SHG_FPO_PASSWORD) { ShgFpoPasswordScreen(navController) }

        // Consumer Flow
        composable(AppRoutes.CONSUMER_LOGIN) { ConsumerLoginScreen(navController) }
        composable(AppRoutes.CONSUMER_REGISTER) { ConsumerRegisterScreen(navController) }
        composable(AppRoutes.SUBSCRIPTION) { SubscriptionScreen(navController) }
        composable(AppRoutes.CONSUMER_RESET_PASSWORD) { ConsumerResetPasswordScreen(navController) }
        composable(
            route = AppRoutes.CONSUMER_VERIFICATION_CODE,
            arguments = listOf(navArgument("email") { type = NavType.StringType })
        ) { backStackEntry ->
            ConsumerVerificationCodeScreen(navController, backStackEntry.arguments?.getString("email"))
        }
        composable(AppRoutes.CONSUMER_SET_NEW_PASSWORD) { ConsumerSetNewPasswordScreen(navController) }
        composable(AppRoutes.CONSUMER_DASHBOARD) { ConsumerDashboardScreen(navController) }
        composable(AppRoutes.CONSUMER_PROFILE) { ConsumerProfileScreen(navController) }
        composable(AppRoutes.CONSUMER_PASSWORD) { ConsumerPasswordScreen(navController) }
        composable(AppRoutes.CONSUMER_NOTIFICATIONS) { ConsumerNotificationsScreen(navController) }
        composable(AppRoutes.CONSUMER_USER_PROFILE) { ConsumerUserProfileScreen(navController) }
        composable(AppRoutes.CONSUMER_EDIT_PROFILE) { ConsumerEditProfileScreen(navController) }
        composable(AppRoutes.CONSUMER_MARKETPLACE) { ConsumerMarketplaceScreen(navController) }
        composable(AppRoutes.MY_CART) { MyCartScreen(navController) }
        composable(
            route = AppRoutes.PRODUCT_DETAILS,
            arguments = listOf(navArgument("productId") { type = NavType.StringType })
        ) { backStackEntry ->
            ProductDetailsScreen(
                navController = navController,
                productId = backStackEntry.arguments?.getString("productId")
            )
        }
        composable(
            route = AppRoutes.TRACEABILITY_JOURNEY,
            arguments = listOf(navArgument("traceabilityId") { type = NavType.StringType })
        ) { backStackEntry ->
            TraceabilityJourneyScreen(
                navController = navController,
                traceabilityId = backStackEntry.arguments?.getString("traceabilityId")
            )
        }
        composable(AppRoutes.CHECKOUT) { CheckoutScreen(navController) }
        composable(
            route = AppRoutes.PAYMENT,
            arguments = listOf(
                navArgument("orderId") { type = NavType.StringType },
                navArgument("amount") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            PaymentScreen(
                navController,
                backStackEntry.arguments?.getString("orderId"),
                backStackEntry.arguments?.getString("amount")
            )
        }
        composable(
            route = AppRoutes.TRACK_ORDER,
            arguments = listOf(navArgument("orderId") { type = NavType.StringType })
        ) { backStackEntry ->
            TrackOrderScreen(navController, backStackEntry.arguments?.getString("orderId"))
        }
        composable(AppRoutes.MY_ORDERS) { MyOrdersScreen(navController) }
        composable(AppRoutes.BAJRA_PRODUCT_DETAILS) { BajraProductDetailsScreen(navController) }
        composable(AppRoutes.BAJRA_CHECKOUT) { BajraCheckoutScreen(navController) }
        composable(AppRoutes.BAJRA_TRACEABILITY) { BajraTraceabilityScreen(navController) }
        composable(AppRoutes.BAJRA_PAYMENT) { BajraPaymentScreen(navController) }
        composable(AppRoutes.BAJRA_TRACK_ORDER) { BajraTrackOrderScreen(navController) }
        composable(AppRoutes.FOXTAIL_PRODUCT_DETAILS) { FoxtailProductDetailsScreen(navController) }
        composable(AppRoutes.FOXTAIL_CHECKOUT) { FoxtailCheckoutScreen(navController) }
        composable(AppRoutes.FOXTAIL_TRACEABILITY) { FoxtailTraceabilityScreen(navController) }
        composable(AppRoutes.FOXTAIL_PAYMENT) { FoxtailPaymentScreen(navController) }
        composable(AppRoutes.FOXTAIL_TRACK_ORDER) { FoxtailTrackOrderScreen(navController) }
        composable(AppRoutes.LITTLE_MILLET_PRODUCT_DETAILS) { LittleMilletProductDetailsScreen(navController) }
        composable(AppRoutes.LITTLE_MILLET_CHECKOUT) { LittleMilletCheckoutScreen(navController) }
        composable(AppRoutes.LITTLE_MILLET_PAYMENT) { LittleMilletPaymentScreen(navController) }
        composable(AppRoutes.LITTLE_MILLET_TRACEABILITY) { LittleMilletTraceabilityScreen(navController) }
        composable(AppRoutes.LITTLE_MILLET_TRACK_ORDER) { LittleMilletTrackOrderScreen(navController) }
        composable(AppRoutes.KODO_MILLET_PRODUCT_DETAILS) { KodoMilletProductDetailsScreen(navController) }
        composable(AppRoutes.KODO_MILLET_CHECKOUT) { KodoMilletCheckoutScreen(navController) }
        composable(AppRoutes.KODO_MILLET_PAYMENT) { KodoMilletPaymentScreen(navController) }
        composable(AppRoutes.KODO_MILLET_TRACEABILITY) { KodoMilletTraceabilityScreen(navController) }
        composable(AppRoutes.KODO_MILLET_TRACK_ORDER) { KodoMilletTrackOrderScreen(navController) }
        composable(AppRoutes.PROSO_MILLET_PRODUCT_DETAILS) { ProsoMilletProductDetailsScreen(navController) }
        composable(AppRoutes.PROSO_MILLET_CHECKOUT) { ProsoMilletCheckoutScreen(navController) }
        composable(AppRoutes.PROSO_MILLET_PAYMENT) { ProsoMilletPaymentScreen(navController) }
        composable(AppRoutes.PROSO_MILLET_TRACEABILITY) { ProsoMilletTraceabilityScreen(navController) }
        composable(AppRoutes.PROSO_MILLET_TRACK_ORDER) { ProsoMilletTrackOrderScreen(navController) }
        composable(AppRoutes.BARNYARD_MILLET_PRODUCT_DETAILS) { BarnyardMilletProductDetailsScreen(navController) }
        composable(AppRoutes.BARNYARD_MILLET_CHECKOUT) { BarnyardMilletCheckoutScreen(navController) }
        composable(AppRoutes.BARNYARD_MILLET_PAYMENT) { BarnyardMilletPaymentScreen(navController) }
        composable(AppRoutes.BARNYARD_MILLET_TRACEABILITY) { BarnyardMilletTraceabilityScreen(navController) }
        composable(AppRoutes.BARNYARD_MILLET_TRACK_ORDER) { BarnyardMilletTrackOrderScreen(navController) }
        composable(AppRoutes.ORDERS_RECEIVED) { OrdersReceivedScreen(navController) }
        composable(AppRoutes.FINGER_MILLET_ORDER_DETAILS) { FingerMilletOrderDetailsScreen(navController) }
        composable(AppRoutes.FOXTAIL_MILLET_ORDER_DETAILS) { FoxtailMilletOrderDetailsScreen(navController) }

        // Other Screens
        composable(AppRoutes.NOTIFICATIONS) { NotificationsScreen(navController) }
        composable(AppRoutes.USER_PROFILE) { UserProfileScreen(navController) }
        composable(AppRoutes.EDIT_PROFILE) { EditProfileScreen(navController) }
        composable(AppRoutes.ADD_MILLET_SUPPLY) { AddMillets1upplyScreen(navController) }
        composable(AppRoutes.MY_SUPPLY_LIST) { MySupplyListScreen(navController) }
        composable(AppRoutes.ORDERS_RECEIVED) { OrdersReceivedScreen(navController) }
        composable(
            AppRoutes.ORDER_DETAILS,
            arguments = listOf(navArgument("orderId") { type = NavType.StringType })
        ) { backStackEntry ->
            OrderDetailsScreen(navController, backStackEntry.arguments?.getString("orderId"))
        }
        composable(AppRoutes.PAYMENT_HISTORY) { PaymentHistoryScreen(navController) }
        composable(
            route = AppRoutes.RECEIPT_DETAILS,
            arguments = listOf(navArgument("paymentId") { type = NavType.IntType })
        ) { backStackEntry ->
            val paymentId = backStackEntry.arguments?.getInt("paymentId") ?: 0
            ReceiptDetailsScreen(navController, paymentId)
        }
        composable(
            route = AppRoutes.LOGISTICS_STATUS,
            arguments = listOf(navArgument("supplyId") { type = NavType.StringType })
        ) { backStackEntry ->
            LogisticsStatusScreen(navController, backStackEntry.arguments?.getString("supplyId"))
        }
        composable(AppRoutes.MARKET_PRICES) { MarketPricesScreen(navController) }
        composable(AppRoutes.SALES_SUMMARY) { SalesSummaryScreen(navController) }
        composable(AppRoutes.SHG_USER_PROFILE) { ShgUserProfileScreen(navController) }
        composable(AppRoutes.FARMER_SUPPLIES) { FarmerSuppliesScreen(navController) }
        composable(
            route = AppRoutes.ADD_PRODUCT_LISTING,
            arguments = listOf(
                navArgument("supplyId") { type = NavType.StringType; nullable = true; defaultValue = null },
                navArgument("milletType") { type = NavType.StringType; nullable = true; defaultValue = null },
                navArgument("quantity") { type = NavType.StringType; nullable = true; defaultValue = null },
                navArgument("qualityGrade") { type = NavType.StringType; nullable = true; defaultValue = null }
            )
        ) { backStackEntry ->
            AddProductListingScreen(
                navController = navController,
                argSupplyId = backStackEntry.arguments?.getString("supplyId"),
                argMilletType = backStackEntry.arguments?.getString("milletType"),
                argQuantity = backStackEntry.arguments?.getString("quantity"),
                argQualityGrade = backStackEntry.arguments?.getString("qualityGrade")
            )
        }
        composable(AppRoutes.MY_PRODUCTS) { MyProductsScreen(navController) }
        composable(AppRoutes.LOGISTICS_ASSIGNMENT) { LogisticsAssignmentScreen(navController) }
        composable(AppRoutes.SHG_PAYMENT_HISTORY) { ShgPaymentHistoryScreen(navController) }
        composable(AppRoutes.TRACEABILITY_RECORDS) { TraceabilityRecordsScreen(navController) }
        composable(AppRoutes.SHG_ANALYTICS) { ShgAnalyticsScreen(navController) }
    }
}
