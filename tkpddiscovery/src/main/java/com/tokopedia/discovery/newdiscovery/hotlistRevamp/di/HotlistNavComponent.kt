package com.tokopedia.discovery.newdiscovery.hotlistRevamp.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.discovery.newdiscovery.hotlistRevamp.view.fragment.HotlistNavFragment
import com.tokopedia.topads.sdk.di.TopAdsWishlistModule
import dagger.Component

@HotlistNavScope
@Component(modules = [HotlistNavUseCaseModule::class,
    HotListNavVewModelModule::class,
    TopAdsWishlistModule::class],
        dependencies = [BaseAppComponent::class])
interface HotlistNavComponent {
    fun inject(hotlistNavFragment: HotlistNavFragment)
}