package com.tokopedia.sellerapp.presentation.screen

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.Text
import com.tokopedia.sellerapp.presentation.viewmodel.SharedViewModel
import com.tokopedia.sellerapp.util.UiState

@Composable
fun NewOrderListScreen(
    sharedViewModel: SharedViewModel,
) {
    val orderList by sharedViewModel.newOrderList.collectAsState()

    LazyColumn {
        item {
            Button(onClick = { 

            }) {
                when(orderList) {
                    is UiState.Success -> {
                        // Needs to be updated with required information
                        Text(text = orderList.data?.firstOrNull()?.orderId.orEmpty())
                    }
                    else -> {
                        Text(text = "Test")
                    }
                }
            }
        }
    }
}

