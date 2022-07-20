package com.tokopedia.sellerapp.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.tokopedia.sellerapp.navigation.SetupNavigation
import com.tokopedia.sellerapp.presentation.theme.SellerApp_ComposeTheme

@ExperimentalAnimationApi
@ExperimentalMaterialApi
class SellerAppActivity : ComponentActivity() {

    private lateinit var navController: NavHostController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SellerApp_ComposeTheme {
                navController = rememberAnimatedNavController()
                SetupNavigation(
                    navController = navController
                )
            }
        }
    }
}
