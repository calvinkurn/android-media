package com.tokopedia.sellerapp.navigation

import SplashScreen
import androidx.navigation.NavGraphBuilder
import androidx.wear.compose.navigation.composable
import com.tokopedia.sellerapp.presentation.screen.HomeScreen
import com.tokopedia.sellerapp.util.Constants

fun NavGraphBuilder.splashComposable(
    navigateToHomeScreen: () -> Unit
) {
    composable(
        route = Constants.SPLASH_SCREEN
    ) {
        SplashScreen(navigateToHomeScreen)
    }
}

fun NavGraphBuilder.homeComposable() {
    composable(
        route = Constants.HOME_SCREEN
    ) {
        HomeScreen()
    }
}