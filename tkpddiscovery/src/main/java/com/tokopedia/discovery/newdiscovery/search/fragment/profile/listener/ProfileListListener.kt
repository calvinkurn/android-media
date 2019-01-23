package com.tokopedia.discovery.newdiscovery.search.fragment.profile.listener

import com.tokopedia.discovery.newdiscovery.base.EmptyStateListener
import com.tokopedia.discovery.newdiscovery.search.fragment.profile.viewmodel.ProfileViewModel

interface ProfileListListener : EmptyStateListener{
    abstract fun onFollowButtonClicked(adapterPosition: Int,
                                       profileModel: ProfileViewModel)
}
