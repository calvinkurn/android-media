package com.tokopedia.sellerapp.navigation

import androidx.navigation.NavController
import com.tokopedia.sellerapp.util.ScreenConstant.FORMAT_NAVIGATION_PATH
import com.tokopedia.sellerapp.util.ScreenConstant.HOME_SCREEN
import com.tokopedia.sellerapp.util.ScreenConstant.NEW_ORDER_DETAIL_SCREEN
import com.tokopedia.sellerapp.util.ScreenConstant.NEW_ORDER_LIST_SCREEN
import com.tokopedia.sellerapp.util.ScreenConstant.NEW_ORDER_SUMMARY_SCREEN
import com.tokopedia.sellerapp.util.ScreenConstant.SPLASH_SCREEN

class ScreenNavigation(navController: NavController) {
    val toHomeScreen: () -> Unit = {
        navController.navigate(route = HOME_SCREEN) {
            popUpTo(SPLASH_SCREEN) {
                inclusive = true
            }
        }
    }
    val toNewOrderSummaryScreen: (dataKey: String) -> Unit = {
        navController.navigate(
            route = FORMAT_NAVIGATION_PATH.format(NEW_ORDER_SUMMARY_SCREEN, it)
        )
    }
    val toNewOrderListScreen: (dataKey: String) -> Unit = {
        navController.navigate(
            route = FORMAT_NAVIGATION_PATH.format(NEW_ORDER_LIST_SCREEN, it)
        )
    }
    val toNewOrderDetailScreen: () -> Unit = {
        navController.navigate(route = NEW_ORDER_DETAIL_SCREEN)
    }
}