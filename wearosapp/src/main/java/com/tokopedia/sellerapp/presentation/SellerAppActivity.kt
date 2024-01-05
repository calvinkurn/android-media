package com.tokopedia.sellerapp.presentation

import SetupNavigation
import WearAppTheme
import android.content.Intent
import android.net.Uri
import android.os.Build
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
import androidx.wear.remote.interactions.RemoteActivityHelper
import com.google.android.gms.wearable.CapabilityClient
import com.google.android.gms.wearable.CapabilityInfo
import com.google.android.gms.wearable.MessageEvent
import com.google.android.gms.wearable.Wearable
import com.newrelic.agent.android.NewRelic
import com.tokopedia.keys.Keys
import com.tokopedia.logger.LogManager
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.sellerapp.R
import com.tokopedia.sellerapp.data.datasource.remote.ActivityMessageListener
import com.tokopedia.sellerapp.data.datasource.remote.ClientMessageDatasource
import com.tokopedia.sellerapp.presentation.screen.ConnectionFailureScreen
import com.tokopedia.sellerapp.presentation.screen.STATE
import com.tokopedia.sellerapp.presentation.screen.StateStatus
import com.tokopedia.sellerapp.presentation.screen.getMessageBasedOnState
import com.tokopedia.sellerapp.presentation.viewmodel.SharedViewModel
import com.tokopedia.sellerapp.util.CapabilityConstant.CAPABILITY_PHONE_APP
import com.tokopedia.sellerapp.util.MarketURIConstant
import com.tokopedia.sellerapp.util.MessageConstant
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
        private const val transitionDelay = 800L
    }

    private lateinit var navController: NavHostController

    @Inject
    lateinit var clientMessageDatasource: ClientMessageDatasource

    @Inject
    lateinit var remoteActivityHelper: RemoteActivityHelper

    private val sharedViewModel: SharedViewModel by viewModels()

    private val phoneStateFlow = mutableStateOf(STATE.CONNECTED)
    private val phoneStateProgressFlow = mutableStateOf(timeoutStartProgress)
    private val phoneConnectionFailed = mutableStateOf(false)
    private var timer: CountDownTimer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        NewRelic.withApplicationToken(Keys.NEW_RELIC_TOKEN_SA)
            .start(this.application)
        super.onCreate(savedInstanceState)

        val remoteConfig = FirebaseRemoteConfigImpl(this)
        remoteConfig.fetch(getRemoteConfigListener())

        lifecycleScope.launch {
            sharedViewModel.ifPhoneHasApp.collect {
                it?.let {
                    if (!it) {
                        phoneStateFlow.value = STATE.COMPANION_NOT_INSTALLED
                        phoneStateProgressFlow.value = timeoutMaxProgress
                        timer?.cancel()
                    }
                }
            }
        }
        setContent {
            WearAppTheme {
                val phoneConnectionStatusIsFailed by remember { phoneConnectionFailed }
                var phoneStateStatus by remember { phoneStateFlow }

                var isStateStatusVisible by remember { mutableStateOf(true) }
                if (!phoneConnectionStatusIsFailed) {
                    navController = rememberSwipeDismissableNavController()
                    SetupNavigation(
                        navController = navController,
                        sharedViewModel = sharedViewModel,
                        remoteActivityHelper = remoteActivityHelper
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
                        else if (phoneStateFlow.value == STATE.COMPANION_NOT_INSTALLED) {
                            mutableStateOf(stringResource(id = R.string.state_button_install_app))}
                        else {
                            mutableStateOf(stringResource(id = R.string.state_button_retry))},
                        stateAction = if (phoneStateFlow.value == STATE.COMPANION_NOT_LOGIN) { mutableStateOf({
                            sharedViewModel.openLoginPageInApp()
                            finish()
                        }) } else if (phoneStateFlow.value == STATE.COMPANION_NOT_INSTALLED) { mutableStateOf({
                            val intent = Intent(Intent.ACTION_VIEW)
                                .addCategory(Intent.CATEGORY_BROWSABLE)
                                .setData(Uri.parse(MarketURIConstant.MARKET_TOKOPEDIA))

                            remoteActivityHelper.startRemoteActivity(intent)
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

                ServerLogger.log(
                    Priority.P2, SharedViewModel.TAG_WEAROS_OPEN_SCREEN, mapOf(
                        SharedViewModel.DEVICE_MODEL to Build.MODEL,
                        SharedViewModel.WEAR_OS_APP_STATE to data
                    ))
            }
            MessageConstant.ACCEPT_BULK_ORDER_PATH ->  {
                sharedViewModel.setAcceptOrderSuccess()
            }
            else -> { }
        }
    }

    fun checkCompanionState() {
        phoneStateProgressFlow.value = timeoutStartProgress
        phoneStateFlow.value = STATE.SYNC
        startStateTimeoutTimer()
        sharedViewModel.checkIfPhoneHasApp()
    }

    private fun getRemoteConfigListener(): RemoteConfig.Listener? {
        return object : RemoteConfig.Listener {
            override fun onComplete(remoteConfig: RemoteConfig) {
                val logManager = LogManager.instance
                logManager?.refreshConfig()

                ServerLogger.log(
                    Priority.P2, SharedViewModel.TAG_WEAROS_OPEN_APP, mapOf(
                        SharedViewModel.DEVICE_MODEL to Build.MODEL
                    ))
            }

            override fun onError(e: Exception) {}
        }
    }

    private fun startStateTimeoutTimer() {
        timer = object: CountDownTimer(timeoutPhoneStateDelay, timeoutPhoneStateTickInterval) {
            override fun onTick(millisUntilFinished: Long) {
                phoneStateProgressFlow.value = phoneStateProgressFlow.value + timeoutAdditionalProgress
            }

            override fun onFinish() {
                if (phoneStateFlow.value == STATE.SYNC) {
                    phoneStateFlow.value = STATE.COMPANION_NOT_REACHABLE
                    phoneStateProgressFlow.value = timeoutMaxProgress
                    validatePhoneState()
                }

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
