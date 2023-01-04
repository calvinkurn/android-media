package com.tokopedia.sellerapp.presentation.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.CircularProgressIndicator
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import com.tokopedia.sellerapp.navigation.ScreenNavigation
import com.tokopedia.sellerapp.presentation.theme.NEST_LAYOUT_LVL1
import com.tokopedia.sellerapp.presentation.theme.NEST_LAYOUT_LVL3
import com.tokopedia.sellerapp.presentation.theme.NestLightGN500
import com.tokopedia.sellerapp.presentation.viewmodel.SharedViewModel
import com.tokopedia.sellerapp.util.UiState
import kotlinx.coroutines.delay

@Composable
fun AcceptOrderScreen(
    screenNavigation: ScreenNavigation,
    sharedViewModel: SharedViewModel,
    listOrderId: List<String>
) {
    LaunchedEffect(Unit) {
        sharedViewModel.sendRequestAcceptBulkOrder(listOrderId)
    }
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        val acceptOrderStatus by sharedViewModel.acceptBulkOrder.collectAsState()
        when (acceptOrderStatus) {
            is UiState.Success -> {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = com.tokopedia.sellerapp.R.drawable.ic_check_success_accept_order),
                        contentDescription = "ImageCheckAcceptOrder",
                        modifier = Modifier
                            .width(36.dp)
                            .height(27.dp)
                    )
                    Text(
                        "Pesanan diterima",
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(
                                horizontal = NEST_LAYOUT_LVL3,
                                vertical = NEST_LAYOUT_LVL1,
                            ),
                    )
                    FinishScreenAfterDelay(screenNavigation)
                }
            }
            is UiState.Loading -> {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .height(20.dp)
                            .width(20.dp),
                        startAngle = 90f,
                        strokeWidth = 3.dp,
                        indicatorColor = NestLightGN500,
                        trackColor = MaterialTheme.colors.onBackground
                            .copy(alpha = 0f)
                    )
                    Text(
                        "Memproses",
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(
                                horizontal = NEST_LAYOUT_LVL3,
                                vertical = NEST_LAYOUT_LVL1,
                            ),
                    )
                }
            }
            else -> {}
        }
    }
}

@Composable
fun FinishScreenAfterDelay(screenNavigation: ScreenNavigation) {
    LaunchedEffect(Unit) {
        val successOrderDelayTime = 2000L
        delay(successOrderDelayTime)
        screenNavigation.popBackStack()
    }
}
