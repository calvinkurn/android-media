/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.tokopedia.tkpd

import android.content.Intent
import android.util.Log
import com.google.android.gms.wearable.DataEventBuffer
import com.google.android.gms.wearable.MessageEvent
import com.google.android.gms.wearable.Wearable
import com.google.android.gms.wearable.WearableListenerService
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


class DataLayerListenerService : WearableListenerService() {


    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    override fun onDataChanged(dataEvents: DataEventBuffer) {
        super.onDataChanged(dataEvents)
    }

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "Service created")

    }

    override fun onMessageReceived(messageEvent: MessageEvent) {
        super.onMessageReceived(messageEvent)

        when (messageEvent.path) {
            MESSAGE_CLIENT_START_ACTIVITY -> {
                startActivity(
                    Intent(this, MainActivity::class.java)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                )
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }

    companion object {
        private const val TAG = "DataLayerService"

        private const val START_ACTIVITY_PATH = "/start-activity"
        private const val DATA_ITEM_RECEIVED_PATH = "/data-item-received"

        const val DATA_CLIENT_PATH = "/data-client-message"
        const val DATA_CLIENT_MESSAGE_KEY = "devara"

        const val DATA_CLIENT_PATH_FROM_WATCH = "/data-client-message-watch"
        const val DATA_CLIENT_MESSAGE_KEY_FROM_WATCH = "devara-watch"

        const val MESSAGE_CLIENT_MESSAGE_PATH = "/data-message"
        const val MESSAGE_CLIENT_START_ACTIVITY = "/start-activity"

        const val IMAGE_KEY = "photo"
    }
}
