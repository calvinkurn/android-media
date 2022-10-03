package com.tokopedia.sellerapp.presentation

import SetupNavigation
import WearAppTheme
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.mutableStateOf
import androidx.navigation.NavHostController
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import com.google.android.gms.wearable.CapabilityClient
import com.google.android.gms.wearable.CapabilityInfo
import com.google.android.gms.wearable.MessageEvent
import com.google.android.gms.wearable.Wearable
import com.tokopedia.sellerapp.data.datasource.remote.ActivityMessageListener
import com.tokopedia.sellerapp.data.datasource.remote.ClientMessageDatasource
import com.tokopedia.sellerapp.presentation.screen.STATE
import com.tokopedia.sellerapp.presentation.screen.StateStatus
import com.tokopedia.sellerapp.presentation.viewmodel.SharedViewModel
import com.tokopedia.sellerapp.util.CapabilityConstant.CAPABILITY_PHONE_APP
import com.tokopedia.sellerapp.util.MessageConstant
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SellerAppActivity : ComponentActivity(), CapabilityClient.OnCapabilityChangedListener, ActivityMessageListener {

    private lateinit var navController: NavHostController

    @Inject
    lateinit var clientMessageDatasource: ClientMessageDatasource

    private val sharedViewModel: SharedViewModel by viewModels()

    private val phoneStateFlow = mutableStateOf(STATE.CONNECTED)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WearAppTheme {
                navController = rememberSwipeDismissableNavController()
                SetupNavigation(
                    navController = navController,
                    sharedViewModel = sharedViewModel,
                )
                StateStatus(state = phoneStateFlow)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        clientMessageDatasource.addMessageClientListener()
        clientMessageDatasource.addActivityMessageListener(this)
        checkCompanionState()
        Wearable.getCapabilityClient(this).addListener(this, CAPABILITY_PHONE_APP)
    }

    override fun onPause() {
        super.onPause()
        clientMessageDatasource.removeMessageClientListener()
        clientMessageDatasource.removeActivityMessageListener()
    }

    override fun onCapabilityChanged(p0: CapabilityInfo) {
    }

    override fun onMessageReceived(messageEvent: MessageEvent) {
        val data = messageEvent.data.decodeToString()

        when(messageEvent.path) {
            MessageConstant.GET_PHONE_STATE -> phoneStateFlow.value = STATE.getStateByString(data)
            else -> { }
        }
    }

    fun checkCompanionState() {
        phoneStateFlow.value = STATE.SYNC
        sharedViewModel.checkPhoneState()
    }
}
