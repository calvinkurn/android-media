package com.tokopedia.sellerapp.presentation.screen

import androidx.compose.Composable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.ChipDefaults
import androidx.wear.compose.material.Text
import com.google.android.gms.wearable.MessageClient
import com.google.android.gms.wearable.NodeClient
import com.tokopedia.sellerapp.presentation.model.NotificationItem
import com.tokopedia.sellerapp.presentation.theme.ChipGrayColor
import com.tokopedia.sellerapp.presentation.viewmodel.SharedViewModel
import com.tokopedia.sellerapp.util.Action
import com.tokopedia.sellerapp.util.UiState

@Composable
fun NotificationScreen(
    sharedViewModel: SharedViewModel,
    messageClient: MessageClient,
    nodeClient: NodeClient
) {
//    val notificationList by sharedViewModel.notificationList.collectAsState()
//    sharedViewModel.sendAction(
//        action = Action.GET_NOTIFICATION_LIST,
//        messageClient = messageClient,
//        nodeClient = nodeClient
//    )

//    LazyColumn {
//        item {
//            Button(onClick = {
//
//            }) {
//                when(orderList) {
//                    is UiState.Success -> {
//                        Text(text = orderList.data?.firstOrNull().orEmpty())
//                    }
//                    else -> {
//                        Text(text = "Test")
//                    }
//                }
//            }
//        }
//    }

}



//@Composable
//fun NotificationChip(modifier: Modifier = Modifier, notificationItem: NotificationItem) {
//    Chip(
//        onClick = { /*TODO*/ },
//        colors = ChipDefaults.chipColors(backgroundColor = ChipGrayColor)
//    ) {
//
//    }
//}