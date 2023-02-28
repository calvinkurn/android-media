package com.tokopedia.sellerapp.presentation.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.*
import com.skydoves.landscapist.glide.GlideImage
import com.tokopedia.sellerapp.R
import com.tokopedia.sellerapp.domain.model.NotificationModel
import com.tokopedia.sellerapp.presentation.theme.ChipGrayColor
import com.tokopedia.sellerapp.presentation.theme.DP_14
import com.tokopedia.sellerapp.presentation.theme.DP_6
import com.tokopedia.sellerapp.presentation.viewmodel.SharedViewModel
import com.tokopedia.sellerapp.util.UiState

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
                    state = scalingLazyListState,
                    notificationDetail = notificationDetail.data ?: NotificationModel()
                )
            }
            else -> {}
        }
    }
}

@Composable
private fun NotificationDetailLayout(
    state: ScalingLazyListState,
    notificationDetail: NotificationModel
) {
    ScalingLazyColumn(state = state) {
        item {
            ListHeader { }
        }
        item {
            NotificationDetailCard(notificationDetail = notificationDetail)
        }
        item {
            if (notificationDetail.infoThumbnailUrl.isNotBlank()) {
                NotificationDetailImage(imageUrl = notificationDetail.infoThumbnailUrl)
            }
        }
    }
}

@Composable
private fun NotificationDetailCard(notificationDetail: NotificationModel) {
    Card(
        onClick = {},
        backgroundPainter = CardDefaults.cardBackgroundPainter(
            MaterialTheme.colors.background,
            MaterialTheme.colors.background
        ),
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp),
        enabled = false
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CompositionLocalProvider(
                    LocalTextStyle provides MaterialTheme.typography.caption1
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_seller_toped),
                        contentDescription = "App icon",
                        modifier = Modifier
                            .size(CardDefaults.AppImageSize)
                            .wrapContentSize(align = Alignment.Center),
                        tint = Color.Unspecified
                    )
                    Spacer(modifier = Modifier.width(6.dp))

                    CompositionLocalProvider(
                        LocalContentColor provides MaterialTheme.colors.onSurfaceVariant
                    ) {
                        Text(stringResource(id = R.string.home_title))
                    }
                }
                Spacer(modifier = Modifier.weight(1.0f))
                CompositionLocalProvider(
                    LocalContentColor provides MaterialTheme.colors.onSurfaceVariant,
                    LocalTextStyle provides MaterialTheme.typography.caption1
                ) {
                    Text(text = notificationDetail.getTimeRelative(), fontSize = 10.sp)
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
            Row {
                CompositionLocalProvider(
                    LocalContentColor provides MaterialTheme.colors.onSurface,
                    LocalTextStyle provides MaterialTheme.typography.title3
                ) {
                    Text(notificationDetail.title)
                }
            }
            if (notificationDetail.shortDescription.isNotBlank()) {
                CompositionLocalProvider(
                    LocalContentColor provides MaterialTheme.colors.onSurfaceVariant,
                    LocalTextStyle provides MaterialTheme.typography.body1
                ) {
                    Text(text = notificationDetail.shortDescription)
                }
            }
        }
    }
}

@Composable
private fun NotificationDetailImage(imageUrl: String) {
    GlideImage(
        imageModel = imageUrl,
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .requiredHeight(height = 68.dp)
            .wrapContentSize(align = Alignment.Center)
    )
}

@Composable
private fun NotificationDetailCtaButton() {
    Button(
        colors = ButtonDefaults.buttonColors(backgroundColor = ChipGrayColor),
        onClick = {}
    ) {
        Text(
            text = stringResource(id = R.string.notification_list_text_open_from_phone),
            modifier = Modifier.padding(DP_14, DP_6)
        )
    }
}
