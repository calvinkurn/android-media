package com.tokopedia.discovery.newdiscovery.search.fragment.profile.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.discovery.newdiscovery.search.fragment.product.adapter.viewholder.EmptySearchViewHolder
import com.tokopedia.discovery.newdiscovery.search.fragment.profile.viewmodel.EmptySearchProfileModel
import com.tokopedia.discovery.newdiscovery.search.fragment.profile.adapter.typefactory.ProfileListTypeFactory
import com.tokopedia.discovery.newdiscovery.search.fragment.profile.adapter.viewholder.EmptySearchProfileViewHolder
import com.tokopedia.discovery.newdiscovery.search.fragment.profile.adapter.viewholder.ProfileViewHolder
import com.tokopedia.discovery.newdiscovery.search.fragment.profile.adapter.viewholder.TotalSearchCountViewHolder
import com.tokopedia.discovery.newdiscovery.search.fragment.profile.listener.ProfileListListener
import com.tokopedia.discovery.newdiscovery.search.fragment.profile.viewmodel.ProfileViewModel
import com.tokopedia.discovery.newdiscovery.search.fragment.profile.viewmodel.TotalSearchCountViewModel

class ProfileListTypeFactoryImpl(val profileListListener: ProfileListListener) : BaseAdapterTypeFactory() , ProfileListTypeFactory{

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<*> {
        return if (type == ProfileViewHolder.LAYOUT) {
            ProfileViewHolder(parent, profileListListener)
        } else if (type == EmptySearchViewHolder.LAYOUT) {
            EmptySearchProfileViewHolder(parent, profileListListener)
        } else if (type == TotalSearchCountViewHolder.LAYOUT) {
            TotalSearchCountViewHolder(parent)
        }
        else {
            super.createViewHolder(parent, type)
        }
    }

    override fun type(profileViewModel: ProfileViewModel): Int {
        return ProfileViewHolder.LAYOUT
    }

    override fun type(emptySearchProfileModel: EmptySearchProfileModel): Int {
        return EmptySearchViewHolder.LAYOUT
    }

    override fun type(totalSearchCountViewModel: TotalSearchCountViewModel): Int {
        return TotalSearchCountViewHolder.LAYOUT
    }
}