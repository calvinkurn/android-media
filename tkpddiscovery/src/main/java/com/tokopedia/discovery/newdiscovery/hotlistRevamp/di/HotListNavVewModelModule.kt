package com.tokopedia.discovery.newdiscovery.hotlistRevamp.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.discovery.newdiscovery.hotlistRevamp.viewmodel.HotlistNavViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
@HotlistNavScope
abstract class HotListNavVewModelModule {

    @Binds
    @HotlistNavScope
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @HotlistNavScope
    @ViewModelKey(HotlistNavViewModel::class)
    internal abstract fun categoryNavViewModel(viewModel: HotlistNavViewModel): ViewModel
}