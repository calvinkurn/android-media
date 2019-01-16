package com.tokopedia.discovery.newdiscovery.search.fragment.profile.adapter.typefactory

import android.view.View
import com.tokopedia.core.base.adapter.viewholders.AbstractViewHolder
import com.tokopedia.discovery.newdiscovery.search.fragment.SearchSectionTypeFactoryImpl
import com.tokopedia.discovery.newdiscovery.search.fragment.product.adapter.viewholder.EmptySearchViewHolder
import com.tokopedia.discovery.newdiscovery.search.fragment.product.viewmodel.EmptySearchModel
import com.tokopedia.discovery.newdiscovery.search.fragment.profile.adapter.viewholder.ProfileViewHolder
import com.tokopedia.discovery.newdiscovery.search.fragment.profile.viewmodel.ProfileViewModel

class ProfileListTypeFactoryImpl : SearchSectionTypeFactoryImpl(), ProfileListTypeFactory  {
    override fun createViewHolder(parent: View?, type: Int): AbstractViewHolder<*> {
        val viewHolder : AbstractViewHolder<*>
        when(type){
            ProfileViewHolder.LAYOUT -> {
                viewHolder = ProfileViewHolder(parent)
            }
            else -> viewHolder = super.createViewHolder(parent, type)
        }
        return viewHolder
    }

    override fun type(profileViewModel: ProfileViewModel): Int {
        return ProfileViewHolder.LAYOUT
    }

    override fun type(emptySearchModel: EmptySearchModel?): Int {
        return EmptySearchViewHolder.LAYOUT
    }
}