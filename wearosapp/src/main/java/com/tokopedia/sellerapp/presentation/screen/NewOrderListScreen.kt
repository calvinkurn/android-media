package com.tokopedia.sellerapp.presentation.screen

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.Text
import com.google.android.gms.wearable.MessageClient
import com.google.android.gms.wearable.NodeClient
import com.tokopedia.sellerapp.presentation.viewmodel.SharedViewModel
import com.tokopedia.sellerapp.util.Action
import com.tokopedia.sellerapp.util.UiState

@Composable
fun NewOrderListScreen(
    sharedViewModel: SharedViewModel,
    messageClient: MessageClient,
    nodeClient: NodeClient
) {
    val orderList by sharedViewModel.orderList.collectAsState()

    sharedViewModel.sendAction(
        action = Action.GET_ORDER_LIST,
        messageClient = messageClient,
        nodeClient = nodeClient
    )

    LazyColumn {
        item {
            Button(onClick = { 

            }) {
                when(orderList) {
                    is UiState.Success -> {
                        Text(text = orderList.data?.firstOrNull().orEmpty())
                    }
                    else -> {
                        Text(text = "Test")
                    }
                }
            }
        }
    }
}

