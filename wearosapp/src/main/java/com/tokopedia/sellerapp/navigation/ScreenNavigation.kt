package com.tokopedia.sellerapp.navigation

import androidx.navigation.NavController
import com.tokopedia.sellerapp.util.Constants.HOME_SCREEN
import com.tokopedia.sellerapp.util.Constants.SPLASH_SCREEN

class ScreenNavigation(navController: NavController) {
    val splashToHomeScreen: () -> Unit = {
        navController.navigate(route = HOME_SCREEN) {
            popUpTo(SPLASH_SCREEN) {
                inclusive = true
            }
        }
    }
}