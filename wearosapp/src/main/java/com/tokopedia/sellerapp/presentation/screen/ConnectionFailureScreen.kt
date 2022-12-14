package com.tokopedia.sellerapp.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.Text
import com.tokopedia.sellerapp.presentation.theme.NEST_LAYOUT_LVL1
import com.tokopedia.sellerapp.presentation.theme.NEST_LAYOUT_LVL3

@Composable
fun ConnectionFailureScreen(
    stateMessage: MutableState<String>,
    stateBtnText: MutableState<String>,
    stateAction: MutableState<() -> Unit>
) {
    val message by remember { stateMessage }
    val btnText by remember { stateBtnText }
    val action by remember { stateAction }

    Column(
        modifier = Modifier
            .fillMaxHeight(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            "$message. You might not be able to access all the feature.",
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(
                    horizontal = NEST_LAYOUT_LVL3,
                    vertical = NEST_LAYOUT_LVL1,
                ),
        )

        Button(
            onClick = {
                      action.invoke()
            },
        ) {
            Text(text = btnText)
        }
    }
}

@Preview(
    device = Devices.WEAR_OS_LARGE_ROUND,
    showSystemUi = true,
    backgroundColor = 0xff000000,
    showBackground = true
)
@Composable
fun AppNotReachable() {
    ConnectionFailureScreen(
        stateMessage = mutableStateOf("Companion device not reachable"),
        stateBtnText = mutableStateOf("Retry"),
        stateAction = mutableStateOf({})
    )
}