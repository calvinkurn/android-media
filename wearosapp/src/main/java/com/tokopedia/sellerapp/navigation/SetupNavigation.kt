import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import com.tokopedia.sellerapp.navigation.ScreenNavigation
import com.tokopedia.sellerapp.navigation.homeComposable
import com.tokopedia.sellerapp.navigation.newOrderDetailComposable
import com.tokopedia.sellerapp.navigation.splashComposable
import com.tokopedia.sellerapp.util.ScreenConstant.SPLASH_SCREEN

@Composable
fun SetupNavigation(
    navController: NavHostController
) {
    val nav = ScreenNavigation(navController)
    SwipeDismissableNavHost(
        navController = navController,
        startDestination = SPLASH_SCREEN
    ) {
        splashComposable(
            navigateToHomeScreen = nav.splashToHomeScreen
        )
        homeComposable()
        newOrderDetailComposable()
    }
}