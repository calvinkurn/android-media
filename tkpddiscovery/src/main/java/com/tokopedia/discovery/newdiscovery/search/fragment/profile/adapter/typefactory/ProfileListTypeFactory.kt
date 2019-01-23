package com.tokopedia.discovery.newdiscovery.search.fragment.profile.adapter.typefactory
import com.tokopedia.discovery.newdiscovery.search.fragment.profile.viewmodel.EmptySearchProfileModel
import com.tokopedia.discovery.newdiscovery.search.fragment.profile.viewmodel.ProfileViewModel
import com.tokopedia.discovery.newdiscovery.search.fragment.profile.viewmodel.TotalSearchCountViewModel

interface ProfileListTypeFactory {
    fun type(profileViewModel: ProfileViewModel): Int
    fun type(emptySearchProfileModel: EmptySearchProfileModel): Int
    fun type(totalSearchCountViewModel: TotalSearchCountViewModel): Int
}