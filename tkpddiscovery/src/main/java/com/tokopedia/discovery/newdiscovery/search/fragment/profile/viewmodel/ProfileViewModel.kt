package com.tokopedia.discovery.newdiscovery.search.fragment.profile.viewmodel

import com.google.android.gms.tagmanager.DataLayer
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
        var post_count: Int,
        var position: Int
        ) : Visitable<ProfileListTypeFactoryImpl> {

        val KEY_ID = "id"
        val KEY_NAME = "name"
        val KEY_CREATIVE = "creative"
        val KEY_POSITION = "position"

        val VAL_NAME = "/search result - profile"

        override fun type(typeFactory: ProfileListTypeFactoryImpl?): Int {
                return typeFactory!!.type(this)
        }

        fun getTrackingObject() : Any {
            return DataLayer.mapOf(
                    KEY_ID, id,
                    KEY_NAME, VAL_NAME,
                    KEY_CREATIVE, name.toLowerCase(),
                    KEY_POSITION, position
            )
        }
}