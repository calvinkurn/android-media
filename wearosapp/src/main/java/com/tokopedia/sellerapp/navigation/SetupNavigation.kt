import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import com.google.android.gms.wearable.MessageClient
import com.google.android.gms.wearable.NodeClient
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
        newOrderSummaryScreenComposable(
            navigateToNewOrderList = nav.toNewOrderListScreen
        )
        newOrderListComposable(
            sharedViewModel = sharedViewModel,
        )
        newOrderDetailComposable()
        appNotInstalledScreenComposable(
            sharedViewModel = sharedViewModel
        )
    }
}