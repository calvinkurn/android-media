package com.tokopedia.sellerapp.presentation

import SetupNavigation
import WearAppTheme
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.navigation.NavHostController
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import com.tokopedia.sellerapp.data.datasource.remote.ClientMessageDatasource
import com.tokopedia.sellerapp.presentation.viewmodel.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SellerAppActivity : ComponentActivity() {

    private lateinit var navController: NavHostController

    @Inject
    lateinit var clientMessageDatasource: ClientMessageDatasource

    private val sharedViewModel: SharedViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WearAppTheme {
                navController = rememberSwipeDismissableNavController()
                SetupNavigation(
                    navController = navController,
                    sharedViewModel = sharedViewModel,
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()
        clientMessageDatasource.addMessageClientListener()
    }

    override fun onPause() {
        super.onPause()
        clientMessageDatasource.removeMessageClientListener()
    }
}
