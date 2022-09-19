package com.tokopedia.sellerapp.navigation

import NewOrderSummaryScreen
import SplashScreen
import androidx.navigation.NavGraphBuilder
import androidx.wear.compose.navigation.composable
import com.google.android.gms.wearable.MessageClient
import com.google.android.gms.wearable.NodeClient
import com.tokopedia.sellerapp.presentation.screen.HomeScreen
import com.tokopedia.sellerapp.presentation.screen.NewOrderDetailScreen
import com.tokopedia.sellerapp.presentation.screen.NewOrderListScreen
import com.tokopedia.sellerapp.presentation.viewmodel.SharedViewModel
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
    navigateToNotification: () -> Unit,
    navigateToNewOrderSummary: () -> Unit
) {
    composable(
        route = ScreenConstant.HOME_SCREEN
    ) {
        HomeScreen(
            navigateToNotification = navigateToNotification,
            navigateToNewOrderSummary = navigateToNewOrderSummary
        )
    }
}

fun NavGraphBuilder.notificationComposable(
    navigateToNotification: () -> Unit
) {
    composable(
        route = ScreenConstant.NOTIFICATION
    ) {
//        NotificationS
    }
}

fun NavGraphBuilder.newOrderListComposable(
    sharedViewModel: SharedViewModel,
    messageClient: MessageClient,
    nodeClient: NodeClient
) {
    composable(
        route = ScreenConstant.NEW_ORDER_LIST_SCREEN
    ) {
        NewOrderListScreen(
            sharedViewModel = sharedViewModel,
            messageClient = messageClient,
            nodeClient = nodeClient
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