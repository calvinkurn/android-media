package com.tokopedia.discovery.newdiscovery.search.fragment.profile.listener

import com.tokopedia.discovery.newdiscovery.base.EmptyStateListener
import com.tokopedia.discovery.newdiscovery.search.fragment.profile.viewmodel.ProfileViewModel

interface ProfileListListener : EmptyStateListener{
    fun onFollowButtonClicked(adapterPosition: Int,
                                       profileModel: ProfileViewModel)
    fun onHandleProfileClick(profileModel: ProfileViewModel)
}
