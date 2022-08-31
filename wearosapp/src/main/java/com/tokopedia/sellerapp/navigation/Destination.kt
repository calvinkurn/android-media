package com.tokopedia.sellerapp.navigation

import NewOrderSummaryScreen
import SplashScreen
import androidx.navigation.NavGraphBuilder
import androidx.wear.compose.navigation.composable
import com.tokopedia.sellerapp.presentation.screen.HomeScreen
import com.tokopedia.sellerapp.presentation.screen.NewOrderDetailScreen
import com.tokopedia.sellerapp.util.ScreenConstant

fun NavGraphBuilder.splashComposable(
    navigateToHomeScreen: () -> Unit
) {
    composable(
        route = ScreenConstant.SPLASH_SCREEN
    ) {
        SplashScreen(navigateToHomeScreen)
    }
}

fun NavGraphBuilder.homeComposable(
    navigateToNewOrderSummary: () -> Unit
) {
    composable(
        route = ScreenConstant.HOME_SCREEN
    ) {
        HomeScreen(navigateToNewOrderSummary)
    }
}

fun NavGraphBuilder.newOrderDetailComposable() {
    composable(
        route = ScreenConstant.NEW_ORDER_DETAIL_SCREEN
    ) {
        NewOrderDetailScreen()
    }
}

fun NavGraphBuilder.newOrderSummaryScreenComposable(
    navigateToNewOrderList: () -> Unit
) {
    composable(
        route = ScreenConstant.NEW_ORDER_SUMMARY_SCREEN
    ) {
        NewOrderSummaryScreen(navigateToNewOrderList)
    }
}