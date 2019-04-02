package com.tokopedia.tkpdpdp.estimasiongkir.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.tkpdpdp.estimasiongkir.presentation.fragment.RatesEstimationDetailFragment

import dagger.Component

@RatesEstimationScope
@Component(modules = arrayOf(RatesEstimationModule::class), dependencies = arrayOf(BaseAppComponent::class))
interface RatesEstimationComponent {
    fun inject(fragment: RatesEstimationDetailFragment)
}
