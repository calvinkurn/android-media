package com.tokopedia.sellerapp.navigation

import NewOrderSummaryScreen
import SplashScreen
import androidx.navigation.NavGraphBuilder
import androidx.wear.compose.navigation.composable
import com.tokopedia.sellerapp.presentation.screen.AppNotInstalledScreen
import com.tokopedia.sellerapp.presentation.screen.HomeScreen
import com.tokopedia.sellerapp.presentation.screen.NewOrderDetailScreen
import com.tokopedia.sellerapp.presentation.screen.NewOrderListScreen
import com.tokopedia.sellerapp.presentation.viewmodel.SharedViewModel
import com.tokopedia.sellerapp.util.ScreenConstant
import com.tokopedia.sellerapp.util.ScreenConstant.DATAKEY_ARGS
import com.tokopedia.sellerapp.util.ScreenConstant.FORMAT_NAVIGATION_PATH_PARAM
import com.tokopedia.sellerapp.util.ScreenConstant.NEW_ORDER_DETAIL_SCREEN
import com.tokopedia.sellerapp.util.ScreenConstant.NEW_ORDER_LIST_SCREEN
import com.tokopedia.sellerapp.util.ScreenConstant.NEW_ORDER_SUMMARY_SCREEN

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
    screenNavigation: ScreenNavigation,
    sharedViewModel: SharedViewModel,
) {
    composable(
        route = FORMAT_NAVIGATION_PATH_PARAM.format(NEW_ORDER_LIST_SCREEN, DATAKEY_ARGS)
    ) { backStackEntry ->
        NewOrderListScreen(
            screenNavigation = screenNavigation,
            sharedViewModel = sharedViewModel,
            dataKey = backStackEntry.arguments?.getString(DATAKEY_ARGS).orEmpty()
        )
    }
}

fun NavGraphBuilder.newOrderDetailComposable(
    screenNavigation: ScreenNavigation,
    sharedViewModel: SharedViewModel,
) {
    composable(
        route = FORMAT_NAVIGATION_PATH_PARAM.format(NEW_ORDER_DETAIL_SCREEN, DATAKEY_ARGS)
    ) { backStackEntry ->
        NewOrderDetailScreen(
            screenNavigation = screenNavigation,
            sharedViewModel = sharedViewModel,
            orderId = backStackEntry.arguments?.getString(DATAKEY_ARGS).orEmpty()
        )
    }
}

fun NavGraphBuilder.newOrderSummaryScreenComposable(
    navigateToNewOrderList: (dataKey: String) -> Unit
) {
    composable(
        route = FORMAT_NAVIGATION_PATH_PARAM.format(NEW_ORDER_SUMMARY_SCREEN, DATAKEY_ARGS)
    ) { backStackEntry ->
        NewOrderSummaryScreen(
            navigateToNewOrderList = navigateToNewOrderList,
            dataKey = backStackEntry.arguments?.getString(DATAKEY_ARGS).orEmpty()
        )
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