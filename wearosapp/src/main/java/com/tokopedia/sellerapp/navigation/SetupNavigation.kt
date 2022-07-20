package com.tokopedia.sellerapp.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.tokopedia.sellerapp.navigation.destination.homeComposable
import com.tokopedia.sellerapp.navigation.destination.splashComposable
import com.tokopedia.sellerapp.util.Constants.SPLASH_SCREEN

@ExperimentalAnimationApi
@ExperimentalMaterialApi
@Composable
fun SetupNavigation(
    navController: NavHostController
) {
    val screen = Screens(navController = navController)
    AnimatedNavHost(
        navController = navController,
        startDestination = SPLASH_SCREEN
    ) {
        splashComposable(
            navigateToHomeScreen = screen.splash
        )
        homeComposable()
    }
}