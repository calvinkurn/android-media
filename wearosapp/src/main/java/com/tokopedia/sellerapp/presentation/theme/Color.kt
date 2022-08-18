package com.tokopedia.sellerapp.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.wear.compose.material.Colors

val Colors.splashScreenBackgroundColor
    @Composable
    get() = if (isSystemInDarkTheme()) NestDarkG500 else NestLightG500

val Colors.defaultBackgroundColor
    @Composable
    get() = if (isSystemInDarkTheme()) NestDarkN0 else NestLightN0

val Purple200 = Color(0xFFBB86FC)
val Purple500 = Color(0xFF6200EE)
val Purple700 = Color(0xFF3700B3)
val Teal200 = Color(0xFF03DAC5)
val NestLightG500 = Color(0xFF03AC0E)
val NestDarkG500 = Color(0xFF50BA47)
val NestLightN0 = Color(0xFFFFFFFF)
val NestDarkN0 = Color(0xFF1D1F22)
val Grey = Color(0xFF202124)
val LightGrey = Color(0xFFBDC1C6)
val NestLightBlue = Color(0xFFAECBFA)


