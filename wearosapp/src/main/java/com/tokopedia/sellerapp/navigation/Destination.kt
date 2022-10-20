package com.tokopedia.sellerapp.navigation

import OrderSummaryScreen
import SplashScreen
import androidx.compose.runtime.mutableStateOf
import androidx.navigation.NavGraphBuilder
import androidx.wear.compose.navigation.composable
import com.tokopedia.sellerapp.presentation.screen.*
import com.tokopedia.sellerapp.presentation.viewmodel.SharedViewModel
import com.tokopedia.sellerapp.util.ScreenConstant
import com.tokopedia.sellerapp.util.ScreenConstant.ACCEPT_ORDER_SCREEN
import com.tokopedia.sellerapp.util.ScreenConstant.DATAKEY_ARGS
import com.tokopedia.sellerapp.util.ScreenConstant.FORMAT_NAVIGATION_PATH_PARAM
import com.tokopedia.sellerapp.util.ScreenConstant.LIST_ORDER_ID_ARGS
import com.tokopedia.sellerapp.util.ScreenConstant.NOTIFICATION_DETAIL_SCREEN
import com.tokopedia.sellerapp.util.ScreenConstant.ORDER_DETAIL_SCREEN
import com.tokopedia.sellerapp.util.ScreenConstant.ORDER_LIST_SCREEN
import com.tokopedia.sellerapp.util.ScreenConstant.ORDER_SUMMARY_SCREEN

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

fun NavGraphBuilder.orderListComposable(
    screenNavigation: ScreenNavigation,
    sharedViewModel: SharedViewModel,
) {
    composable(
        route = FORMAT_NAVIGATION_PATH_PARAM.format(ORDER_LIST_SCREEN, DATAKEY_ARGS)
    ) { backStackEntry ->
        OrderListScreen(
            screenNavigation = screenNavigation,
            sharedViewModel = sharedViewModel,
            orderType = backStackEntry.arguments?.getString(DATAKEY_ARGS).orEmpty()
        )
    }
}

fun NavGraphBuilder.orderDetailComposable(
    screenNavigation: ScreenNavigation,
    sharedViewModel: SharedViewModel,
) {
    composable(
        route = FORMAT_NAVIGATION_PATH_PARAM.format(ORDER_DETAIL_SCREEN, DATAKEY_ARGS)
    ) { backStackEntry ->
        NewOrderDetailScreen(
            screenNavigation = screenNavigation,
            sharedViewModel = sharedViewModel,
            orderId = backStackEntry.arguments?.getString(DATAKEY_ARGS).orEmpty()
        )
    }
}

fun NavGraphBuilder.orderSummaryScreenComposable(
    navigateToNewOrderList: (dataKey: String) -> Unit,
    sharedViewModel: SharedViewModel
) {
    composable(
        route = FORMAT_NAVIGATION_PATH_PARAM.format(ORDER_SUMMARY_SCREEN, DATAKEY_ARGS)
    ) { backStackEntry ->
        OrderSummaryScreen(
            navigateToNewOrderList = navigateToNewOrderList,
            dataKey = backStackEntry.arguments?.getString(DATAKEY_ARGS).orEmpty(),
            sharedViewModel = sharedViewModel
        )
    }
}

fun NavGraphBuilder.notificationListComposable(
    screenNavigation: ScreenNavigation,
    sharedViewModel: SharedViewModel
) {
    composable(
        route = ScreenConstant.NOTIFICATION_LIST_SCREEN
    ) {
        NotificationListScreen(
            screenNavigation = screenNavigation,
            sharedViewModel = sharedViewModel
        )
    }
}

fun NavGraphBuilder.notificationDetailComposable(
    sharedViewModel: SharedViewModel
) {
    composable(
        route = FORMAT_NAVIGATION_PATH_PARAM.format(NOTIFICATION_DETAIL_SCREEN, DATAKEY_ARGS)
    ) { backStackEntry ->
        NotificationDetailScreen(
            sharedViewModel = sharedViewModel,
            notificationId = backStackEntry.arguments?.getString(DATAKEY_ARGS).orEmpty()
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

fun NavGraphBuilder.connectionFailedScreenComposable(
    sharedViewModel: SharedViewModel,
) {
    composable(
        route = ScreenConstant.CONNECTION_FAILED_SCREEN
    ) {
        val message = sharedViewModel.currentState.value.data?.let {
            it.getState().getMessageBasedOnState()
        } ?: ""

        ConnectionFailureScreen(
            mutableStateOf(message),
            mutableStateOf("Retry"),
            mutableStateOf({})
        )
    }
}

fun NavGraphBuilder.acceptOrderScreenComposable(
    sharedViewModel: SharedViewModel,
    screenNavigation: ScreenNavigation
) {
    composable(
        route = FORMAT_NAVIGATION_PATH_PARAM.format(ACCEPT_ORDER_SCREEN, LIST_ORDER_ID_ARGS)
    ) { backStackEntry->
        AcceptOrderScreen(
            sharedViewModel = sharedViewModel,
            screenNavigation = screenNavigation,
            listOrderId = backStackEntry.arguments?.getString(LIST_ORDER_ID_ARGS)?.split(",").orEmpty()
        )
    }
}
