package com.tokopedia.sellerapp.presentation.screen

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.*
import com.tokopedia.sellerapp.presentation.model.MenuItem
import com.tokopedia.sellerapp.presentation.theme.Grey
import com.tokopedia.sellerapp.presentation.theme.LightGrey
import com.tokopedia.tkpd.R
import com.tokopedia.iconunify.R.drawable as iconR

const val TITLE_NOTIF = "Notifikasi"
const val TITLE_CHAT = "Chat"
const val TITLE_NEW_ORDER = "Pesanan Baru"
const val TITLE_READY_TO_DELIVER = "Siap Dikirim"
val ICON_NOTIF = iconR.iconunify_bell
val ICON_CHAT = iconR.iconunify_chat
val ICON_NEW_ORDER = iconR.iconunify_product
val ICON_READY_TO_DELIVER = iconR.iconunify_product_move

private fun generateMenuItem() = listOf(
    MenuItem(TITLE_NOTIF, 1, ICON_NOTIF),
    MenuItem(TITLE_CHAT, 1, ICON_CHAT),
    MenuItem(TITLE_NEW_ORDER, 4, ICON_NEW_ORDER),
    MenuItem(TITLE_READY_TO_DELIVER, 2, ICON_READY_TO_DELIVER),
)

@Composable
fun HomeScreen() {
    val listState = rememberScalingLazyListState()

    val menuItems = generateMenuItem()

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
                        color = LightGrey,
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
        onClick = { /* ... */ },
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
            backgroundColor = Grey,
        ),
    )
}