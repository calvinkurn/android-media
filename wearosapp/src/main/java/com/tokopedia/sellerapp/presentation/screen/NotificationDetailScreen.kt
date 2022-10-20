package com.tokopedia.sellerapp.presentation.screen

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.wear.compose.material.*
import com.tokopedia.sellerapp.domain.model.NotificationModel
import com.tokopedia.sellerapp.presentation.theme.ChipGrayColor
import com.tokopedia.sellerapp.presentation.theme.DP_14
import com.tokopedia.sellerapp.presentation.theme.DP_6
import com.tokopedia.sellerapp.presentation.viewmodel.SharedViewModel
import com.tokopedia.sellerapp.util.UiState
import com.tokopedia.tkpd.R

@Composable
fun NotificationDetailScreen(
    sharedViewModel: SharedViewModel,
    notificationId: String
) {
    getNotificationDetail(sharedViewModel, notificationId)
    NotificationDetailScaffold(
        scalingLazyListState = ScalingLazyListState(),
        sharedViewModel = sharedViewModel
    )
}

private fun getNotificationDetail(
    sharedViewModel: SharedViewModel,
    notificationId: String
) {
    sharedViewModel.getNotificationDetail(notificationId)
}

@Composable
private fun NotificationDetailScaffold(
    scalingLazyListState: ScalingLazyListState,
    sharedViewModel: SharedViewModel
) {
    Scaffold(
        timeText = {
            if (!scalingLazyListState.isScrollInProgress) {
                TimeText()
            }
        }
    ) {
        val notificationDetail by sharedViewModel.notificationDetail.collectAsState()
        when (notificationDetail) {
            is UiState.Success -> {
                NotificationDetailLayout(
                    notificationDetail.data ?: NotificationModel()
                )
            }
            else -> {}
        }
    }
}

@Composable
private fun NotificationDetailLayout(notificationDetail: NotificationModel) {
    ScalingLazyColumn {
        item {
            ListHeader { }
        }
        item {
            NotificationDetailCard(notificationDetail = notificationDetail)
        }
        item {
            NotificationDetailCtaButton()
        }
    }
}

@Composable
private fun NotificationDetailCard(notificationDetail: NotificationModel) {
    AppCard(
        appImage = {
            Icon(
                painter = painterResource(id = R.drawable.ic_seller_app),
                contentDescription = "App icon",
                modifier = Modifier
                    .size(CardDefaults.AppImageSize)
                    .wrapContentSize(align = Alignment.Center)
            )
        },
        appName = {
            Text(stringResource(id = R.string.home_title))
        },
        title = {
            Text(notificationDetail.title)
        },
        content = {
            Text(notificationDetail.shortDescription)
        },
        backgroundPainter = CardDefaults.cardBackgroundPainter(
            MaterialTheme.colors.background,
            MaterialTheme.colors.background
        ),
        time = {
            Text("now")
        },
        enabled = false,
        onClick = {}
    )
}

@Composable
private fun NotificationDetailCtaButton() {
    Button(
        colors = ButtonDefaults.buttonColors(backgroundColor = ChipGrayColor),
        onClick = {}) {
        Text(
            text = stringResource(id = R.string.notification_list_text_open_from_phone),
            modifier = Modifier.padding(DP_14, DP_6)
        )
    }
}