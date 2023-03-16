import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.wear.compose.material.MaterialTheme
import com.tokopedia.sellerapp.presentation.theme.DP_0
import com.tokopedia.sellerapp.presentation.theme.DP_100
import com.tokopedia.sellerapp.presentation.theme.defaultBackgroundColor
import com.tokopedia.sellerapp.presentation.theme.splashScreenBackgroundColor
import com.tokopedia.sellerapp.presentation.viewmodel.SharedViewModel
import com.tokopedia.sellerapp.util.NumberConstant.ANIMATION_SPLASH_DURATION
import com.tokopedia.sellerapp.util.NumberConstant.DELAY_SPLASH_DURATION
import com.tokopedia.sellerapp.util.NumberConstant.START_LOGO_ALPHA_TARGET
import com.tokopedia.sellerapp.util.NumberConstant.STOP_LOGO_ALPHA_TARGET
import com.tokopedia.sellerapp.R
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    navigateToHomeScreen: () -> Unit,
    navigateToAppNotInstalledScreen: () -> Unit,
    sharedViewModel: SharedViewModel,
) {
    var startAnimation by remember { mutableStateOf(false) }
    val ifPhoneHasApp = sharedViewModel.ifPhoneHasApp.collectAsState()

    val offState by animateDpAsState(
        targetValue = if (startAnimation) DP_0 else DP_100,
        animationSpec = tween(
            durationMillis = ANIMATION_SPLASH_DURATION
        )
    )

    val alphaState by animateFloatAsState(
        targetValue = if (startAnimation) START_LOGO_ALPHA_TARGET else STOP_LOGO_ALPHA_TARGET,
        animationSpec = tween(
            durationMillis = ANIMATION_SPLASH_DURATION
        )
    )

    var animVisibleState by remember { mutableStateOf(true) }

    LaunchedEffect(key1 = true) {
        startAnimation = true
        delay(DELAY_SPLASH_DURATION)
        animVisibleState = false
        delay(DELAY_SPLASH_DURATION)
        ifPhoneHasApp.value?.let {
            if (it) {
                navigateToHomeScreen()
            } else {
                navigateToAppNotInstalledScreen()
            }
        }
    }

    AnimatedVisibility(
        visible = animVisibleState,
        enter = fadeIn(
            animationSpec = tween(
                durationMillis = ANIMATION_SPLASH_DURATION
            )
        ),
        exit = fadeOut(
            animationSpec = tween(
                durationMillis = ANIMATION_SPLASH_DURATION
            )
        ),
        modifier = Modifier.background(
            color = MaterialTheme.colors.defaultBackgroundColor
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = MaterialTheme.colors.splashScreenBackgroundColor
                ),
            contentAlignment = Alignment.Center
        ) {
            Image(
                modifier = Modifier
                    .size(DP_100)
                    .offset(y = offState)
                    .alpha(alpha = alphaState),
                painter = painterResource(id = R.drawable.logo_tkpd_white),
                contentDescription = stringResource(id = R.string.splash_screen_logo_content_description)
            )
        }
    }
}
