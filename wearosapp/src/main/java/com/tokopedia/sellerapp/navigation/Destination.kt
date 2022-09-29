package com.tokopedia.sellerapp.navigation

import NewOrderSummaryScreen
import SplashScreen
import androidx.navigation.NavGraphBuilder
import androidx.wear.compose.navigation.composable
import com.google.android.gms.wearable.MessageClient
import com.google.android.gms.wearable.NodeClient
import com.tokopedia.sellerapp.presentation.screen.AppNotInstalledScreen
import com.tokopedia.sellerapp.presentation.screen.HomeScreen
import com.tokopedia.sellerapp.presentation.screen.NewOrderDetailScreen
import com.tokopedia.sellerapp.presentation.screen.NewOrderListScreen
import com.tokopedia.sellerapp.presentation.viewmodel.SharedViewModel
import com.tokopedia.sellerapp.util.ScreenConstant

fun NavGraphBuilder.splashComposable(
    navigateToHomeScreen: () -> Unit,
    navigateToAppNotInstalledScreen: () -> Unit,
    sharedViewModel: SharedViewModel,
) {
    composable(
        route = ScreenConstant.SPLASH_SCREEN
    ) {
        SplashScreen(navigateToHomeScreen, navigateToAppNotInstalledScreen, sharedViewModel)
    }
}

fun NavGraphBuilder.homeComposable(
    screenNavigation: ScreenNavigation,
    sharedViewModel: SharedViewModel
) {
    composable(
        route = ScreenConstant.HOME_SCREEN
    ) {
        HomeScreen(screenNavigation, sharedViewModel)
    }
}

fun NavGraphBuilder.newOrderListComposable(
    sharedViewModel: SharedViewModel,
) {
    composable(
        route = ScreenConstant.NEW_ORDER_LIST_SCREEN
    ) {
        NewOrderListScreen(
            sharedViewModel = sharedViewModel,
        )
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

fun NavGraphBuilder.appNotInstalledScreenComposable(
    sharedViewModel: SharedViewModel,
) {
    composable(
        route = ScreenConstant.APP_NOT_INSTALLED_SCREEN
    ) {
        AppNotInstalledScreen(sharedViewModel)
    }
}