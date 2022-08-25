package com.tokopedia.sellerapp.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import com.tokopedia.sellerapp.presentation.theme.NestLightNN0
import com.tokopedia.sellerapp.presentation.theme.defaultBackgroundColor

@Composable
fun HomeScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colors.defaultBackgroundColor),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Hello World", color = NestLightNN0)
    }
}