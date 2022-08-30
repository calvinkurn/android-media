package com.tokopedia.sellerapp.presentation

import SetupNavigation
import WearAppTheme
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import com.tokopedia.sellerapp.presentation.viewmodel.SellerAppViewModel
import com.tokopedia.sellerapp.presentation.viewmodel.SellerAppViewModelFactory

class SellerAppActivity : ComponentActivity() {

    private lateinit var navController: NavHostController
    private lateinit var sellerAppViewModel: SellerAppViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WearAppTheme {
                navController = rememberSwipeDismissableNavController()
                SetupNavigation(navController = navController)
            }
        }

        initViewModel()
        observeOrderListData()
        loadData()
    }

    private fun initViewModel() {
        sellerAppViewModel =
            ViewModelProvider(this, SellerAppViewModelFactory(this))
                .get(SellerAppViewModel::class.java)
    }

    private fun observeOrderListData() {
        sellerAppViewModel.getOrderListDataState().observe(this) {

        }
    }

    private fun loadData() {
        sellerAppViewModel.getOrderListData(SellerAppViewModel.Action.GET_ORDER_LIST)
    }
}
