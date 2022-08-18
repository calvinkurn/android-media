package com.tokopedia.sellerapp.presentation.screen

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.*
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.sellerapp.presentation.model.MenuItem
import com.tokopedia.sellerapp.presentation.theme.Grey
import com.tokopedia.sellerapp.presentation.theme.LightGrey
import com.tokopedia.tkpd.R
import com.tokopedia.iconunify.R.drawable as iconR

@Composable
fun HomeScreen() {
    val listState = rememberScalingLazyListState()
    val menuItems = listOf(
        MenuItem("Notifikasi", 1, iconR.iconunify_bell),
        MenuItem("Chat", 1, iconR.iconunify_chat),
        MenuItem("Pesanan Baru", 4, iconR.iconunify_product),
        MenuItem("Siap Dikirim", 2, iconR.iconunify_product_move),
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
                        text = stringResource(id = R.string.home_title)
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