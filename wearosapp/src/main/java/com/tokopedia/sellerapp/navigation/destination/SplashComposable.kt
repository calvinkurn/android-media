package com.tokopedia.sellerapp.navigation.destination

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideOutVertically
import androidx.navigation.NavGraphBuilder
import com.google.accompanist.navigation.animation.composable
import com.tokopedia.sellerapp.presentation.screen.SplashScreen
import com.tokopedia.sellerapp.util.Constants.SPLASH_SCREEN

@ExperimentalAnimationApi
fun NavGraphBuilder.splashComposable(
    navigateToHomeScreen: () -> Unit
) {
    composable(
        route = SPLASH_SCREEN,
        exitTransition = {
            slideOutVertically (
                animationSpec = tween(500),
                targetOffsetY = { -it }
            )
        }
    ) {
        SplashScreen(navigateToHomeScreen)
    }
}