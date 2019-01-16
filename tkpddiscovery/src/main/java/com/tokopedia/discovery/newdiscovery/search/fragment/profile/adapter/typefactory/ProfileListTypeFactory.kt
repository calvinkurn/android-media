package com.tokopedia.discovery.newdiscovery.search.fragment.profile.adapter.typefactory
import com.tokopedia.discovery.newdiscovery.search.fragment.profile.viewmodel.ProfileViewModel

interface ProfileListTypeFactory {
    fun type(profileViewModel: ProfileViewModel): Int
}