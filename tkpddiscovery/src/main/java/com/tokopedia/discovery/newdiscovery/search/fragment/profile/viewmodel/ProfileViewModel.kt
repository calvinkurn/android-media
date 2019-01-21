package com.tokopedia.discovery.newdiscovery.search.fragment.profile.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.discovery.newdiscovery.search.fragment.profile.adapter.ProfileListAdapter

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
        ) : Visitable<ProfileListAdapter> {
        override fun type(typeFactory: ProfileListAdapter?): Int {
                return typeFactory!!.type(this)
        }
}