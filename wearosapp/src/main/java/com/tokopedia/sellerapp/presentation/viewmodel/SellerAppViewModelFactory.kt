package com.tokopedia.sellerapp.presentation.viewmodel

import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class SellerAppViewModelFactory(
    private val componentActivity: ComponentActivity
): ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SellerAppViewModel::class.java)) {
            return createSellerAppViewModel() as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }

    private fun createSellerAppViewModel(): SellerAppViewModel {
        return SellerAppViewModel(componentActivity)
    }
}