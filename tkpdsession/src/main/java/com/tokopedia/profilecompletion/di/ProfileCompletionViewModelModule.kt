package com.tokopedia.profilecompletion.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelKey
import com.tokopedia.profilecompletion.viewmodel.PinViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * Created by Ade Fulki on 2019-09-12.
 * ade.hadian@tokopedia.com
 */

@Module
@ProfileCompletionScope
abstract class ProfileCompletionViewModelModule {

    @Binds
    @ProfileCompletionScope
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(PinViewModel::class)
    internal abstract fun bindPinViewModel(viewModel: PinViewModel): ViewModel
}