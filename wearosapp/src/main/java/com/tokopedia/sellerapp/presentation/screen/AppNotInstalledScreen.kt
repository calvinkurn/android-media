package com.tokopedia.sellerapp.presentation.screen

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.Text
import androidx.wear.remote.interactions.RemoteActivityHelper
import com.tokopedia.sellerapp.presentation.theme.NEST_LAYOUT_LVL1
import com.tokopedia.sellerapp.presentation.theme.NEST_LAYOUT_LVL3
import com.tokopedia.sellerapp.util.MarketURIConstant

@Composable
fun AppNotInstalledScreen(
    remoteActivityHelper: RemoteActivityHelper
) {
    Column(
        modifier = Modifier
            .fillMaxHeight(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            "App is Not Installed. Do you want to install Tokopedia App on your phone?",
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(
                    horizontal = NEST_LAYOUT_LVL3,
                    vertical = NEST_LAYOUT_LVL1,
                ),
        )

        Button(
            onClick = {
                val intent = Intent(Intent.ACTION_VIEW)
                    .addCategory(Intent.CATEGORY_BROWSABLE)
                    .setData(Uri.parse(MarketURIConstant.MARKET_TOKOPEDIA))

                remoteActivityHelper.startRemoteActivity(intent)
            },
        ) {
            Text(text = "Install")
        }
    }
}