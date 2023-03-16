package com.tokopedia.sellerapp.presentation.screen

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.foundation.*
import androidx.wear.compose.material.*
import com.tokopedia.sellerapp.R

enum class STATE {
    CONNECTED,
    SYNC,
    COMPANION_NOT_LOGIN,
    COMPANION_NOT_REACHABLE,
    COMPANION_NOT_INSTALLED;

    fun getStringState() = when(this) {
        SYNC -> "sync"
        CONNECTED -> "connected"
        COMPANION_NOT_LOGIN -> "companion_not_login"
        COMPANION_NOT_REACHABLE -> "companion_not_reachable"
        COMPANION_NOT_INSTALLED -> "companion_not_installed"
    }

    companion object {
        fun getStateByString(stateString: String) = when(stateString) {
            "sync" -> SYNC
            "connected" -> CONNECTED
            "companion_not_login" -> COMPANION_NOT_LOGIN
            "companion_not_reachable" -> COMPANION_NOT_REACHABLE
            "companion_not_installed" -> COMPANION_NOT_INSTALLED
            else -> SYNC
        }
    }
}

fun STATE.getInitialProgress(): Float {
    return when(this) {
        STATE.SYNC -> 0.2f
        else -> 1f
    }
}

@Composable
fun STATE.getTextColorBasedOnState(): Color {
    var color = colorResource(id = R.color.Unify_GN500)
    when(this) {
        STATE.SYNC -> {
            color = colorResource(id = R.color.Unify_YN300)
        }
        STATE.COMPANION_NOT_LOGIN -> {
            color = colorResource(id = R.color.Unify_RN500)
        }
        STATE.COMPANION_NOT_REACHABLE -> {
            color = colorResource(id = R.color.Unify_RN500)
        }
        STATE.COMPANION_NOT_INSTALLED -> {
            color = colorResource(id = R.color.Unify_RN500)
        }
    }
    return color
}

fun STATE.getMessageBasedOnState(): String {
    var text = "Syncing with companion device..."
    when(this) {
        STATE.SYNC -> {
            text = "Syncing with companion device..."
        }
        STATE.COMPANION_NOT_LOGIN -> {
            text = "Companion device not logged in"
        }
        STATE.COMPANION_NOT_REACHABLE -> {
            text = "Companion device not reachable"
        }
        STATE.COMPANION_NOT_INSTALLED -> {
            text = "Companion device not installed"
        }
        STATE.CONNECTED -> {
            text = "Companion device connected"
        }
    }
    return text
}

@Composable
fun StateStatusComposable(status: MutableState<STATE>) {
        val state by remember { status }
        val stateText = state.getMessageBasedOnState()
        val colorText = state.getTextColorBasedOnState()
        if (LocalConfiguration.current.isScreenRound) {
        val primaryColor = MaterialTheme.colors.primary
        CurvedLayout(
            anchor = 90F,
            anchorType = AnchorType.Center,
            modifier = Modifier.fillMaxSize()
        ) {

            curvedRow {
                curvedText(
                    text = stateText,
                    angularDirection = CurvedDirection.Angular.CounterClockwise,
                    style = CurvedTextStyle(
                        fontSize = 18.sp,
                        color = primaryColor,
                    ),
                    color = colorText,
                    modifier = CurvedModifier
                        .radialGradientBackground(
                            0f to Color.Transparent,
                            0.2f to Color.DarkGray.copy(alpha = 0.2f),
                            0.6f to Color.DarkGray.copy(alpha = 0.2f),
                            0.7f to Color.DarkGray.copy(alpha = 0.05f),
                            1f to Color.Transparent
                        )
                )
            }
        }
    }
    else {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.Bottom
        ) {
            Text(
                modifier = Modifier
                    .padding(bottom = 2.dp)
                    .background(
                        Brush.verticalGradient(
                            0f to Color.Transparent,
                            0.3f to Color.DarkGray.copy(alpha = 0.05f),
                            0.4f to Color.DarkGray.copy(alpha = 0.2f),
                            0.8f to Color.DarkGray.copy(alpha = 0.2f),
                            1f to Color.Transparent
                        )
                    ),
                textAlign = TextAlign.Center,
                color = colorText,
                text = stateText,
                fontSize = 18.sp
            )
        }
    }
}

@Composable
fun StateStatus(state: MutableState<STATE>, progressState: MutableState<Float> = mutableStateOf(0.2f)) {
    StateStatusComposable(state)

    val progress by remember { progressState }
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec
    )

    CircularProgressIndicator(
        progress = animatedProgress,
        modifier = Modifier.fillMaxSize(),
        startAngle = 170f,
        endAngle = 10f,
        strokeWidth = 4.dp,
        indicatorColor = state.value.getTextColorBasedOnState()
    )
}

@Preview(
    device = Devices.WEAR_OS_LARGE_ROUND,
    showSystemUi = true,
    backgroundColor = 0xff000000,
    showBackground = true
)
@Composable
fun StateStatusSyncPreview() {
    StateStatus(state = mutableStateOf(STATE.SYNC))
}

@Preview(
    device = Devices.WEAR_OS_LARGE_ROUND,
    showSystemUi = true,
    backgroundColor = 0xff000000,
    showBackground = true
)
@Composable
fun StateStatusNotLoginPreview() {
    StateStatus(state = mutableStateOf(STATE.COMPANION_NOT_LOGIN))
}

@Preview(
    device = Devices.WEAR_OS_LARGE_ROUND,
    showSystemUi = true,
    backgroundColor = 0xff000000,
    showBackground = true
)
@Composable
fun StateStatusNotInstalledPreview() {
    StateStatus(state = mutableStateOf(STATE.COMPANION_NOT_INSTALLED))
}

@Preview(
    device = Devices.WEAR_OS_LARGE_ROUND,
    showSystemUi = true,
    backgroundColor = 0xff000000,
    showBackground = true
)
@Composable
fun StateStatusNotReachablePreview() {
    StateStatus(state = mutableStateOf(STATE.COMPANION_NOT_REACHABLE))
}
