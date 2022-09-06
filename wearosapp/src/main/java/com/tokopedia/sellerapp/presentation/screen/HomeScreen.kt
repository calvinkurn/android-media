package com.tokopedia.sellerapp.presentation.screen

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.tokopedia.sellerapp.presentation.model.MenuItem
import com.tokopedia.tkpd.R
import com.tokopedia.iconunify.R.drawable as iconR
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.ChipDefaults
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.ListHeader
import androidx.wear.compose.material.PositionIndicator
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.ScalingLazyColumn
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TimeText
import androidx.wear.compose.material.Vignette
import androidx.wear.compose.material.VignettePosition
import androidx.wear.compose.material.rememberScalingLazyListState
import com.tokopedia.sellerapp.presentation.theme.ChipGrayColor
import com.tokopedia.sellerapp.presentation.theme.TextGrayColor

const val TITLE_NOTIF = "Notifikasi"
const val TITLE_CHAT = "Chat"
const val TITLE_NEW_ORDER = "Pesanan Baru"
const val TITLE_READY_TO_DELIVER = "Siap Dikirim"
val ICON_NOTIF = iconR.iconunify_bell
val ICON_CHAT = iconR.iconunify_chat
val ICON_NEW_ORDER = iconR.iconunify_product
val ICON_READY_TO_DELIVER = iconR.iconunify_product_move

private fun generateMenuItem(
    navigateToNotification: () -> Unit,
    navigateToChat: () -> Unit,
    navigateToNewOrderList: () -> Unit,
    navigateToNewOrderSummary: () -> Unit
) = listOf(
    MenuItem(
        title = TITLE_NOTIF,
        unreadCount = 1,
        icon = ICON_NOTIF,
        navigateToThePage = navigateToNotification
    ),
    MenuItem(
        title = TITLE_CHAT,
        unreadCount = 1,
        icon = ICON_CHAT,
        navigateToThePage = navigateToChat
    ),
    MenuItem(
        title = TITLE_NEW_ORDER,
        unreadCount = 4,
        icon = ICON_NEW_ORDER,
        navigateToThePage = navigateToNewOrderList
    ),
    MenuItem(
        title = TITLE_READY_TO_DELIVER,
        unreadCount = 2,
        icon = ICON_READY_TO_DELIVER,
        navigateToThePage = navigateToNewOrderSummary
    )
)

@Composable
fun HomeScreen(
    navigateToNewOrderSummary: () -> Unit
) {
    val listState = rememberScalingLazyListState()

    val menuItems = generateMenuItem(
        navigateToNotification = {},
        navigateToChat = {},
        navigateToNewOrderList = {},
        navigateToNewOrderSummary = navigateToNewOrderSummary
    )

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
        ScalingLazyColumn(
            modifier = Modifier.fillMaxSize(),
            state = listState
        ) {
            item {
                ListHeader {
                    Text(
                        color = TextGrayColor,
                        text = stringResource(id = R.string.home_title)
                    )
                }
            }
            items(menuItems.size){
                MenuChip(Modifier.fillMaxWidth(), menuItems[it])
            }
        }
    }
}

@Composable
fun MenuChip(
    modifier: Modifier = Modifier,
    menuItem: MenuItem
) {
    Chip(
        modifier = modifier,
        onClick = {
            menuItem.navigateToThePage()
        },
        label = {
            Text(
                modifier = Modifier.padding(start = 4.dp),
                text = String.format("%s (%d)", menuItem.title, menuItem.unreadCount),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        icon = {
            val painter = painterResource(id = menuItem.icon)
            Icon(
                modifier = Modifier.size(20.dp),
                painter = painter,
                contentDescription = menuItem.title,
            )
        },
        colors = ChipDefaults.chipColors(
            backgroundColor = ChipGrayColor,
        ),
    )
}