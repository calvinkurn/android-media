package com.tokopedia.sellerapp.navigation

import androidx.navigation.NavController
import com.tokopedia.sellerapp.util.ScreenConstant.HOME_SCREEN
import com.tokopedia.sellerapp.util.ScreenConstant.NEW_ORDER_DETAIL_SCREEN
import com.tokopedia.sellerapp.util.ScreenConstant.SPLASH_SCREEN

class ScreenNavigation(navController: NavController) {
    val splashToHomeScreen: () -> Unit = {
        navController.navigate(route = HOME_SCREEN) {
            popUpTo(SPLASH_SCREEN) {
                inclusive = true
            }
        }
    }
    val newOrderListToDetailScreen: () -> Unit = {
        navController.navigate(route = NEW_ORDER_DETAIL_SCREEN)
    }
}