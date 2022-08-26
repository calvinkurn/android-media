package com.tokopedia.sellerapp.navigation

import androidx.navigation.NavController
import com.tokopedia.sellerapp.util.ScreenConstant.HOME_SCREEN
import com.tokopedia.sellerapp.util.ScreenConstant.NEW_ORDER_DETAIL_SCREEN
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
    val toNewOrderSummaryScreen: () -> Unit = {
        navController.navigate(route = NEW_ORDER_SUMMARY_SCREEN)
    }
    val toNewOrderDetailScreen: () -> Unit = {
        navController.navigate(route = NEW_ORDER_DETAIL_SCREEN)
    }
}