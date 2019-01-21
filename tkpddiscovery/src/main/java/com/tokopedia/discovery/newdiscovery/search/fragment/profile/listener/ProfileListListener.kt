package com.tokopedia.discovery.newdiscovery.search.fragment.profile.listener

import com.tokopedia.discovery.newdiscovery.search.fragment.profile.viewmodel.ProfileViewModel

interface ProfileListListener {
    abstract fun onFavoriteButtonClicked(adapterPosition: Int,
                                         profileModel: ProfileViewModel)
}
