package com.tokopedia.sellerapp.navigation

import androidx.navigation.NavController
import com.tokopedia.sellerapp.util.ScreenConstant.ACCEPT_ORDER_SCREEN
import com.tokopedia.sellerapp.util.ScreenConstant.APP_NOT_INSTALLED_SCREEN
import com.tokopedia.sellerapp.util.ScreenConstant.CONNECTION_FAILED_SCREEN
import com.tokopedia.sellerapp.util.ScreenConstant.FORMAT_NAVIGATION_PATH
import com.tokopedia.sellerapp.util.ScreenConstant.HOME_SCREEN
import com.tokopedia.sellerapp.util.ScreenConstant.NOTIFICATION_DETAIL_SCREEN
import com.tokopedia.sellerapp.util.ScreenConstant.NOTIFICATION_LIST_SCREEN
import com.tokopedia.sellerapp.util.ScreenConstant.ORDER_DETAIL_SCREEN
import com.tokopedia.sellerapp.util.ScreenConstant.ORDER_LIST_SCREEN
import com.tokopedia.sellerapp.util.ScreenConstant.ORDER_SUMMARY_SCREEN
import com.tokopedia.sellerapp.util.ScreenConstant.SPLASH_SCREEN

class ScreenNavigation(navController: NavController) {
    val toHomeScreen: () -> Unit = {
        navController.navigate(route = HOME_SCREEN) {
            popUpTo(SPLASH_SCREEN) {
                inclusive = true
            }
        }
    }
    val toNotificationListScreen: (dataKey: String) -> Unit = {
        navController.navigate(
            route = NOTIFICATION_LIST_SCREEN
        )
    }
    val toNotificationDetailScreen: (dataKey: String) -> Unit = {
        navController.navigate(
            route = FORMAT_NAVIGATION_PATH.format(NOTIFICATION_DETAIL_SCREEN, it)
        )
    }
    val toOrderSummaryScreen: (dataKey: String) -> Unit = {
        navController.navigate(
            route = FORMAT_NAVIGATION_PATH.format(ORDER_SUMMARY_SCREEN, it)
        )
    }
    val toOrderListScreen: (dataKey: String) -> Unit = {
        navController.navigate(
            route = FORMAT_NAVIGATION_PATH.format(ORDER_LIST_SCREEN, it)
        )
    }
    val toOrderDetailScreen: (dataKey: String) -> Unit = {
        navController.navigate(
            route = FORMAT_NAVIGATION_PATH.format(ORDER_DETAIL_SCREEN, it)
        )
    }
    val toAppNotInstalledScreen: () -> Unit = {
        navController.navigate(route = APP_NOT_INSTALLED_SCREEN) {
            popUpTo(SPLASH_SCREEN) {
                inclusive = true
            }
        }
    }
    val toConnectionFailureScreen: () -> Unit = {
        navController.navigate(route = CONNECTION_FAILED_SCREEN) {
            popUpTo(SPLASH_SCREEN) {
                inclusive = true
            }
        }
    }
    val toAcceptOrderScreen: (listOrderId: List<String>) -> Unit = {
        navController.navigate(
            route = FORMAT_NAVIGATION_PATH.format(
                ACCEPT_ORDER_SCREEN,
                it.joinToString(",")
            )
        )
    }
    val popBackStack: () -> Unit = {
        navController.popBackStack()
    }
}
