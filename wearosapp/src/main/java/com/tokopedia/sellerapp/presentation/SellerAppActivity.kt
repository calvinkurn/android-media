package com.tokopedia.sellerapp.presentation

import SetupNavigation
import WearAppTheme
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.navigation.NavHostController
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import com.google.android.gms.wearable.MessageClient
import com.google.android.gms.wearable.NodeClient
import com.google.android.gms.wearable.Wearable
import com.tokopedia.sellerapp.presentation.viewmodel.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SellerAppActivity : ComponentActivity() {

    private lateinit var navController: NavHostController

    private val nodeClient: NodeClient by lazy { Wearable.getNodeClient(this) }
    private val messageClient: MessageClient by lazy { Wearable.getMessageClient(this) }
    private val sharedViewModel: SharedViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WearAppTheme {
                navController = rememberSwipeDismissableNavController()
                SetupNavigation(
                    navController = navController,
                    sharedViewModel = sharedViewModel,
                    messageClient = messageClient,
                    nodeClient = nodeClient
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()
        messageClient.addListener(sharedViewModel)
    }

    override fun onPause() {
        super.onPause()
        messageClient.removeListener(sharedViewModel)
    }
}
