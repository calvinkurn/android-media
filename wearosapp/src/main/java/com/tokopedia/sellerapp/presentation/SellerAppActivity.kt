package com.tokopedia.sellerapp.presentation

import SetupNavigation
import WearAppTheme
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.NavHostController
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import com.tokopedia.sellerapp.util.WearMessageHelper

class SellerAppActivity : ComponentActivity() {

    private lateinit var navController: NavHostController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WearAppTheme {
                navController = rememberSwipeDismissableNavController()
                SetupNavigation(navController = navController)
            }
        }

        WearMessageHelper.sendAction(this, WearMessageHelper.Action.GET_ORDER_LIST, ::onOrderListDataReceived)
    }

    private fun onOrderListDataReceived(data: String) {

    }
}
