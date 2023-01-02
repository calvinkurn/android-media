import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import com.tokopedia.sellerapp.navigation.*
import com.tokopedia.sellerapp.presentation.viewmodel.SharedViewModel
import com.tokopedia.sellerapp.util.ScreenConstant.SPLASH_SCREEN

@Composable
fun SetupNavigation(
    navController: NavHostController,
    sharedViewModel: SharedViewModel,
) {
    val nav = ScreenNavigation(navController)
    SwipeDismissableNavHost(
        navController = navController,
        startDestination = SPLASH_SCREEN
    ) {
        splashComposable(
            navigateToHomeScreen = nav.toHomeScreen,
            navigateToAppNotInstalledScreen = nav.toAppNotInstalledScreen,
            sharedViewModel = sharedViewModel,
        )
        homeComposable(
            screenNavigation = nav,
            sharedViewModel = sharedViewModel
        )
        orderSummaryScreenComposable(
            navigateToNewOrderList = nav.toOrderListScreen,
            sharedViewModel = sharedViewModel
        )
        orderListComposable(
            screenNavigation = nav,
            sharedViewModel = sharedViewModel,
        )
        orderDetailComposable(
            screenNavigation = nav,
            sharedViewModel = sharedViewModel
        )
        appNotInstalledScreenComposable(
            sharedViewModel = sharedViewModel
        )
        connectionFailedScreenComposable(
            sharedViewModel = sharedViewModel
        )
        acceptOrderScreenComposable(
            sharedViewModel = sharedViewModel,
            screenNavigation = nav
        )
    }
}