package com.tokopedia.sellerapp.presentation

import SetupNavigation
import WearAppTheme
import android.os.Bundle
import android.os.CountDownTimer
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.*
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import com.google.android.gms.wearable.CapabilityClient
import com.google.android.gms.wearable.CapabilityInfo
import com.google.android.gms.wearable.MessageEvent
import com.google.android.gms.wearable.Wearable
import com.tokopedia.sellerapp.data.datasource.remote.ActivityMessageListener
import com.tokopedia.sellerapp.data.datasource.remote.ClientMessageDatasource
import com.tokopedia.sellerapp.presentation.screen.ConnectionFailureScreen
import com.tokopedia.sellerapp.presentation.screen.STATE
import com.tokopedia.sellerapp.presentation.screen.StateStatus
import com.tokopedia.sellerapp.presentation.screen.getMessageBasedOnState
import com.tokopedia.sellerapp.presentation.viewmodel.SharedViewModel
import com.tokopedia.sellerapp.util.CapabilityConstant.CAPABILITY_PHONE_APP
import com.tokopedia.sellerapp.util.MessageConstant
import com.tokopedia.tkpd.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SellerAppActivity : ComponentActivity(), CapabilityClient.OnCapabilityChangedListener, ActivityMessageListener {

    companion object {
        private const val timeoutPhoneStateDelay = 3000L
        private const val timeoutPhoneStateTickInterval = 1500L
        private const val timeoutAdditionalProgress = 0.4f
        private const val timeoutMaxProgress = 1f
        private const val timeoutStartProgress = 0f
        private const val transitionDelay = 800f
    }

    private lateinit var navController: NavHostController

    @Inject
    lateinit var clientMessageDatasource: ClientMessageDatasource

    private val sharedViewModel: SharedViewModel by viewModels()

    private val phoneStateFlow = mutableStateOf(STATE.CONNECTED)
    private val phoneStateProgressFlow = mutableStateOf(timeoutStartProgress)
    private val phoneConnectionFailed = mutableStateOf(false)

    private var timer: CountDownTimer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WearAppTheme {
                val phoneConnectionStatusIsFailed by remember { phoneConnectionFailed }
                val phoneStateStatus by remember { phoneStateFlow }

                var isStateStatusVisible by remember { mutableStateOf(true) }
                if (!phoneConnectionStatusIsFailed) {
                    navController = rememberSwipeDismissableNavController()
                    SetupNavigation(
                        navController = navController,
                        sharedViewModel = sharedViewModel,
                    )
                    if (phoneStateStatus == STATE.CONNECTED) {
                        LaunchedEffect(Unit) {
                            delay(transitionDelay)
                            isStateStatusVisible = false
                        }
                    }
                    AnimatedVisibility(
                        visible = isStateStatusVisible,
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        StateStatus(state = phoneStateFlow, progressState = phoneStateProgressFlow)
                    }
                } else {
                    ConnectionFailureScreen(
                        stateMessage = mutableStateOf(phoneStateFlow.value.getMessageBasedOnState()),
                        stateBtnText = if (phoneStateFlow.value == STATE.COMPANION_NOT_LOGIN) {
                            mutableStateOf(stringResource(id = R.string.state_button_login_in_app))}
                        else {
                            mutableStateOf(stringResource(id = R.string.state_button_retry))},
                        stateAction = if (phoneStateFlow.value == STATE.COMPANION_NOT_LOGIN) { mutableStateOf({
                            sharedViewModel.openLoginPageInApp()
                            finish()
                        }) } else { mutableStateOf({
                            phoneConnectionFailed.value = false
                            checkCompanionState()
                        }) }
                    )
                }
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
            MessageConstant.GET_PHONE_STATE -> {
                timer?.cancel()
                phoneStateFlow.value = STATE.getStateByString(data)
                phoneStateProgressFlow.value = timeoutMaxProgress
                validatePhoneState()
            }
            else -> { }
        }
    }

    fun checkCompanionState() {
        phoneStateProgressFlow.value = timeoutStartProgress
        phoneStateFlow.value = STATE.SYNC
        startStateTimeoutTimer()
        sharedViewModel.checkPhoneState()
    }

    private fun startStateTimeoutTimer() {
        timer = object: CountDownTimer(timeoutPhoneStateDelay, timeoutPhoneStateTickInterval) {
            override fun onTick(millisUntilFinished: Long) {
                phoneStateProgressFlow.value = phoneStateProgressFlow.value + timeoutAdditionalProgress
            }

            override fun onFinish() {
                phoneStateFlow.value = STATE.COMPANION_NOT_REACHABLE
                phoneStateProgressFlow.value = timeoutMaxProgress
                validatePhoneState()
                this.cancel()
            }
        }
        timer?.start()
    }

    private fun validatePhoneState() {
        lifecycleScope.launch {
            delay(transitionDelay)
            phoneConnectionFailed.value = phoneStateFlow.value != STATE.CONNECTED
        }
    }
}
