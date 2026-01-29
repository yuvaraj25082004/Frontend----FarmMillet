package com.simats.farmmillet

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation

// This is the authentication flow navigation graph
fun NavGraphBuilder.authFlow(navController: NavController) {
    navigation(startDestination = AppRoutes.WELCOME, route = "auth") {
        composable(AppRoutes.WELCOME) { WelcomeScreen(navController) }
        composable(AppRoutes.ROLE_SELECTION) { RoleSelectionScreen(navController) }

        // Farmer flow
        composable(AppRoutes.FARMER_LOGIN) { FarmerLoginScreen(navController) }
        composable(AppRoutes.FARMER_PROFILE) { FarmerProfileScreen(navController) }
        composable(AppRoutes.FARMER_PASSWORD) { FarmerPasswordScreen(navController) }

        // SHG/FPO flow
        composable(AppRoutes.SHG_FPO_LOGIN) { ShgFpoLoginScreen(navController) }
        composable(AppRoutes.SHG_FPO_PROFILE) { ShgFpoProfileScreen(navController) }
        composable(AppRoutes.SHG_FPO_PASSWORD) { ShgFpoPasswordScreen(navController) }

        // Consumer flow
        composable(AppRoutes.CONSUMER_LOGIN) { ConsumerLoginScreen(navController) }
        composable(AppRoutes.CONSUMER_PROFILE) { ConsumerProfileScreen(navController) }
        composable(AppRoutes.CONSUMER_PASSWORD) { ConsumerPasswordScreen(navController) }

        // Generic Password Recovery
        composable(AppRoutes.RESET_PASSWORD) { ResetPasswordScreen(navController) }
        composable(AppRoutes.VERIFICATION_CODE) { VerificationCodeScreen(navController) }
        composable(AppRoutes.SET_NEW_PASSWORD) { SetNewPasswordScreen(navController) }
    }
}