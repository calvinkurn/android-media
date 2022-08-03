package com.tokopedia.sellerapp.presentation.screen

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.*
import com.tokopedia.sellerapp.presentation.model.MenuItem
import com.tokopedia.sellerapp.presentation.theme.Grey
import com.tokopedia.sellerapp.presentation.theme.LightGrey

@Composable
fun HomeScreen() {
    val listState = rememberScalingLazyListState()
    val menuItems = listOf(
        MenuItem("Notifikasi", 1),
        MenuItem("Chat", 1),
        MenuItem("Pesanan Baru", 4),
        MenuItem("Siap Dikirim", 2),
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
        val contentModifier = Modifier
            .fillMaxWidth()
        ScalingLazyColumn(
            modifier = Modifier.fillMaxSize(),
            state = listState
        ) {
            item {
                ListHeader {
                    Text(
                        color = LightGrey,
                        text = "Tokopedia Seller"
                    )
                }
            }
            items(menuItems.size){
                MenuChip(contentModifier, menuItems[it])
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
            val painter = painterResource(id = com.tokopedia.tkpd.R.drawable.ic_notif)
            Icon(
                modifier = Modifier.size(20.dp),
                painter = painter,
                contentDescription = "Notifikasi",
            )
        },
        colors = ChipDefaults.chipColors(
            backgroundColor = Grey,
        ),
    )
}