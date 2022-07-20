package com.tokopedia.tkpd

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import androidx.wear.compose.material.*
import com.google.android.gms.wearable.DataClient
import com.google.android.gms.wearable.DataEvent
import com.google.android.gms.wearable.DataEventBuffer
import com.google.android.gms.wearable.MessageClient
import com.google.android.gms.wearable.MessageEvent
import com.google.android.gms.wearable.PutDataMapRequest
import com.google.android.gms.wearable.Wearable
import com.tokopedia.tkpd.DataLayerListenerService.Companion.DATA_CLIENT_MESSAGE_KEY_FROM_WATCH
import com.tokopedia.tkpd.DataLayerListenerService.Companion.DATA_CLIENT_PATH
import com.tokopedia.tkpd.DataLayerListenerService.Companion.DATA_CLIENT_PATH_FROM_WATCH
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


class MainActivity : ComponentActivity(),
    DataClient.OnDataChangedListener,
    MessageClient.OnMessageReceivedListener {

    companion object {
        const val TAG = "DevaraFikryTag"
        const val TAG_ZEL = "FrenzelDebug"
    }

    private val dataClient by lazy { Wearable.getDataClient(this) }
    private val messageClient by lazy { Wearable.getMessageClient(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            WearApp()
        }
    }

    override fun onResume() {
        super.onResume()
        dataClient.addListener(this)
        messageClient.addListener(this)
    }

    override fun onPause() {
        super.onPause()
        dataClient.removeListener(this)
    }

    override fun onDataChanged(dataEvents: DataEventBuffer) {
        // Do additional work for specific events
        dataEvents.forEach { dataEvent ->
            when (dataEvent.type) {
                DataEvent.TYPE_CHANGED -> {
                    when (dataEvent.dataItem.uri.path) {
                        DATA_CLIENT_PATH -> {
//                            val message = DataMapItem.fromDataItem(dataEvent.dataItem)
//                                .dataMap
//                                .getString(DATA_CLIENT_MESSAGE_KEY)
//                            binding.tvData.text = message
                        }
                    }
                }
            }
        }
    }

    override fun onMessageReceived(messageEvent: MessageEvent) {
        when(messageEvent.path) {
//            DataLayerListenerService.MESSAGE_CLIENT_MESSAGE_PATH -> {
//                binding.tvMessage.text = messageEvent.data.decodeToString()
//            }
        }
    }

    private fun sendDataToApp(msg: String) {
        lifecycleScope.launch {
            try {
                val request = PutDataMapRequest.create(DATA_CLIENT_PATH_FROM_WATCH).apply {
                    dataMap.putString(DATA_CLIENT_MESSAGE_KEY_FROM_WATCH, msg)
                }
                    .asPutDataRequest()
                    .setUrgent()

                val result = dataClient.putDataItem(request).await()

                Log.d(TAG, "DataItem saved: $result")
            } catch (cancellationException: CancellationException) {
                throw cancellationException
            } catch (exception: Exception) {
                Log.d(TAG, "Saving DataItem failed: $exception")
            }
        }
    }
}


@Composable
fun WearApp() {
    WearAppTheme {
        val listState = rememberScalingLazyListState()

        Scaffold(
            timeText = {
                if (!listState.isScrollInProgress) {
                    TimeText()
                }
            },
            vignette = {
                Vignette(vignettePosition = VignettePosition.TopAndBottom)
            },
            positionIndicator = {
                PositionIndicator(
                    scalingLazyListState = listState
                )
            }
        ) {
            val contentModifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
            ScalingLazyColumn(
                modifier = Modifier.fillMaxSize(),
                autoCentering = AutoCenteringParams(itemIndex = 0),
                state = listState
            ) {
                item { MenuChip(contentModifier, "Notifikasi (1)") }
                item { MenuChip(contentModifier, "Chat (1)") }
            }
        }
    }
}

@Composable
fun MenuChip(
    modifier: Modifier = Modifier,
    text: String
) {
    Chip(
        modifier = modifier,
        onClick = { /* ... */ },
        label = {
            Text(
                text = text,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    )
}