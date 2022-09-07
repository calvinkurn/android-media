import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import com.google.android.gms.wearable.MessageClient
import com.google.android.gms.wearable.NodeClient
import com.tokopedia.sellerapp.navigation.ScreenNavigation
import com.tokopedia.sellerapp.navigation.homeComposable
import com.tokopedia.sellerapp.navigation.newOrderDetailComposable
import com.tokopedia.sellerapp.navigation.newOrderListComposable
import com.tokopedia.sellerapp.navigation.newOrderSummaryScreenComposable
import com.tokopedia.sellerapp.navigation.splashComposable
import com.tokopedia.sellerapp.presentation.viewmodel.SharedViewModel
import com.tokopedia.sellerapp.util.ScreenConstant.SPLASH_SCREEN

@Composable
fun SetupNavigation(
    navController: NavHostController,
    sharedViewModel: SharedViewModel,
    messageClient: MessageClient,
    nodeClient: NodeClient
) {
    val nav = ScreenNavigation(navController)
    SwipeDismissableNavHost(
        navController = navController,
        startDestination = SPLASH_SCREEN
    ) {
        splashComposable(
            navigateToHomeScreen = nav.toHomeScreen
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
            messageClient = messageClient,
            nodeClient = nodeClient
        )
        newOrderDetailComposable()
    }
}