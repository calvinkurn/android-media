package com.tokopedia.sellerapp.presentation.screen

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.tokopedia.sellerapp.presentation.model.MenuItem
import com.tokopedia.sellerapp.R
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.tokopedia.sellerapp.navigation.ScreenNavigation
import com.tokopedia.sellerapp.presentation.model.TITLE_NEW_ORDER
import com.tokopedia.sellerapp.presentation.model.TITLE_NOTIF
import com.tokopedia.sellerapp.presentation.model.TITLE_READY_TO_SHIP
import com.tokopedia.sellerapp.presentation.theme.ChipGrayColor
import com.tokopedia.sellerapp.presentation.theme.TextGrayColor
import com.tokopedia.sellerapp.presentation.viewmodel.SharedViewModel

@Composable
fun HomeScreen(
    screenNavigation: ScreenNavigation,
    sharedViewModel: SharedViewModel
) {
    val menuItems by sharedViewModel.homeMenu.collectAsState()

    val listState = rememberScalingLazyListState()

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
                MenuChip(Modifier.fillMaxWidth(), menuItems[it], screenNavigation)
            }
        }
    }
}

@Composable
fun MenuChip(
    modifier: Modifier = Modifier,
    menuItem: MenuItem,
    navigation: ScreenNavigation
) {
    Chip(
        modifier = modifier,
        onClick = {
            menuClickNavigation(menuItem, navigation)
        },
        label = {
            val labelText = if(menuItem.unreadCount == 0) {
                menuItem.title
            } else {
                String.format("%s (%d)", menuItem.title, menuItem.unreadCount)
            }
            Text(
                modifier = Modifier.padding(start = 4.dp),
                text = labelText,
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

private fun menuClickNavigation(
    menuItem: MenuItem,
    navigation: ScreenNavigation
) {
    when(menuItem.title){
        TITLE_NOTIF -> navigation.toNotificationListScreen(menuItem.dataKey)
        TITLE_NEW_ORDER,
        TITLE_READY_TO_SHIP -> navigation.toOrderSummaryScreen(menuItem.dataKey)
        else -> { }
    }
}
