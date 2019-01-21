package com.tokopedia.discovery.newdiscovery.search.fragment.profile.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.discovery.newdiscovery.search.fragment.product.adapter.viewholder.EmptySearchViewHolder
import com.tokopedia.discovery.newdiscovery.search.fragment.product.viewmodel.EmptySearchModel
import com.tokopedia.discovery.newdiscovery.search.fragment.profile.adapter.typefactory.ProfileListTypeFactory
import com.tokopedia.discovery.newdiscovery.search.fragment.profile.adapter.viewholder.ProfileViewHolder
import com.tokopedia.discovery.newdiscovery.search.fragment.profile.listener.FollowActionListener
import com.tokopedia.discovery.newdiscovery.search.fragment.profile.listener.ProfileListListener
import com.tokopedia.discovery.newdiscovery.search.fragment.profile.viewmodel.ProfileViewModel

class ProfileListAdapter(val profileListListener: ProfileListListener) : BaseAdapterTypeFactory() , ProfileListTypeFactory{

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<*> {
        return if (type == ProfileViewHolder.LAYOUT) {
            ProfileViewHolder(parent, profileListListener)
        } else {
            super.createViewHolder(parent, type)
        }
    }

    override fun type(profileViewModel: ProfileViewModel): Int {
        return ProfileViewHolder.LAYOUT
    }

    fun type(emptySearchModel: EmptySearchModel?): Int {
        return EmptySearchViewHolder.LAYOUT
    }
}