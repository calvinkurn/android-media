package com.tokopedia.discovery.newdiscovery.search.fragment.profile.viewmodel

import android.support.annotation.DrawableRes

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.discovery.newdiscovery.search.fragment.profile.adapter.ProfileListTypeFactoryImpl
import com.tokopedia.discovery.newdynamicfilter.helper.FilterFlagSelectedModel


class EmptySearchProfileModel : Visitable<ProfileListTypeFactoryImpl> {

    @DrawableRes
    var imageRes: Int = 0
    var title: String? = ""
    var content: String? = ""
    var buttonText: String? = ""

    override fun type(typeFactory: ProfileListTypeFactoryImpl): Int {
        return typeFactory.type(this)
    }
}
