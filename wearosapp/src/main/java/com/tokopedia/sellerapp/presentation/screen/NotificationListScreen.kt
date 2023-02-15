package com.tokopedia.sellerapp.presentation.screen

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.*
import com.tokopedia.sellerapp.R
import com.tokopedia.sellerapp.domain.model.NotificationModel
import com.tokopedia.sellerapp.navigation.ScreenNavigation
import com.tokopedia.sellerapp.presentation.theme.ChipGrayColor
import com.tokopedia.sellerapp.presentation.viewmodel.SharedViewModel
import com.tokopedia.sellerapp.util.UiState

@Composable
fun NotificationListScreen(
    screenNavigation: ScreenNavigation,
    sharedViewModel: SharedViewModel
) {
    getNotificationList(sharedViewModel)
    NotificationListScaffold(
        ScalingLazyListState(),
        screenNavigation,
        sharedViewModel
    )
}

private fun getNotificationList(sharedViewModel: SharedViewModel) {
    sharedViewModel.getNotificationList()
}

@Composable
private fun NotificationListScaffold(
    scalingLazyListState: ScalingLazyListState,
    screenNavigation: ScreenNavigation,
    sharedViewModel: SharedViewModel
) {
    Scaffold(
        timeText = {
            if (!scalingLazyListState.isScrollInProgress) {
                TimeText()
            }
        },
        vignette = {
            Vignette(vignettePosition = VignettePosition.TopAndBottom)
        },
        positionIndicator = {
            PositionIndicator(
                scalingLazyListState = scalingLazyListState
            )
        }
    ) {
        val notifications by sharedViewModel.notifications.collectAsState()
        when (notifications) {
            is UiState.Success -> {
                NotificationList(
                    screenNavigation = screenNavigation,
                    notifications = notifications.data ?: listOf(),
                    state = scalingLazyListState
                )
            }
            else -> {}
        }
    }
}

@Composable
private fun NotificationList(
    screenNavigation: ScreenNavigation,
    notifications: List<NotificationModel>,
    state: ScalingLazyListState
) {
    ScalingLazyColumn(
        modifier = Modifier.fillMaxWidth(),
        state = state
    ) {
        item {
            ListHeader { }
        }
        items(notifications.size) { index ->
            AppCard(
                appImage = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_seller_toped),
                        contentDescription = stringResource(id = R.string.content_description_app_icon),
                        modifier = Modifier
                            .size(CardDefaults.AppImageSize)
                            .wrapContentSize(align = Alignment.Center),
                        tint = Color.Unspecified
                    )
                },
                appName = {
                    Text(text = stringResource(id = R.string.home_title))
                },
                title = {
                    Text(notifications[index].title, maxLines = 1, overflow = TextOverflow.Ellipsis)
                },
                content = {
                    Text(
                        notifications[index].shortDescription,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                time = {
                    Text(text = notifications[index].getTimeRelative(), fontSize = 10.sp)
                },
                backgroundPainter = CardDefaults.cardBackgroundPainter(
                    ChipGrayColor,
                    ChipGrayColor
                ),
                onClick = {
                    openNotificationDetail(
                        screenNavigation = screenNavigation,
                        notification = notifications[index]
                    )
                }
            )
        }
    }
}

private fun openNotificationDetail(
    screenNavigation: ScreenNavigation,
    notification: NotificationModel
) {
    screenNavigation.toNotificationDetailScreen(notification.notificationId)
}
