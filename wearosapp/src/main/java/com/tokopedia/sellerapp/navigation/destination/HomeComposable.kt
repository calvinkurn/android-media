package com.tokopedia.sellerapp.navigation.destination

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.navigation.NavGraphBuilder
import com.google.accompanist.navigation.animation.composable
import com.tokopedia.sellerapp.util.Constants.HOME_SCREEN

@ExperimentalAnimationApi
fun NavGraphBuilder.homeComposable() {
    composable(
        route = HOME_SCREEN
    ) {

    }
}