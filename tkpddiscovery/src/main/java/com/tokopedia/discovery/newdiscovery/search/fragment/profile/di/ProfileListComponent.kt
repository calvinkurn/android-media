package com.tokopedia.profile.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.discovery.newdiscovery.search.fragment.profile.ProfileListFragment
import com.tokopedia.discovery.newdiscovery.search.fragment.profile.ProfileListPresenter
import dagger.Component

/**
 * @author by milhamj on 9/21/18.
 */
@ProfileListScope
@Component(
        modules = arrayOf(ProfileListModule::class),
        dependencies = arrayOf(BaseAppComponent::class)
)
interface ProfileListComponent {
    fun inject(fragment: ProfileListFragment)
    fun inject(presenter: ProfileListPresenter)
}