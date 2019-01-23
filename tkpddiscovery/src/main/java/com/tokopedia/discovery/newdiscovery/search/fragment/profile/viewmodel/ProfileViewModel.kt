package com.tokopedia.discovery.newdiscovery.search.fragment.profile.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.discovery.newdiscovery.search.fragment.profile.adapter.ProfileListTypeFactoryImpl

data class ProfileViewModel (
        var id: String,
        var name: String,
        var imgUrl: String,
        var username: String,
        var bio: String,
        var followed: Boolean,
        var isKol: Boolean,
        var isAffiliate: Boolean,
        var following: Int,
        var followers: Int,
        var post_count: Int
        ) : Visitable<ProfileListTypeFactoryImpl> {
        override fun type(typeFactory: ProfileListTypeFactoryImpl?): Int {
                return typeFactory!!.type(this)
        }
}