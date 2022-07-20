package com.tokopedia.sellerapp.navigation

import androidx.navigation.NavController
import com.tokopedia.sellerapp.util.Constants.SPLASH_SCREEN

class Screens(navController: NavController) {
    val splash: () -> Unit = {
        navController.navigate(route = "home") {
            popUpTo(SPLASH_SCREEN) { inclusive = true }
        }
    }

    val orderSummary: (Int) -> Unit = { shopId ->
        navController.navigate(route = "order-summary/$shopId")
    }
}