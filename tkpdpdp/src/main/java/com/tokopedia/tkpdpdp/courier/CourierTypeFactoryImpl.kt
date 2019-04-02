package com.tokopedia.tkpdpdp.courier

import android.view.View

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder

class CourierTypeFactoryImpl : BaseAdapterTypeFactory(), CourierTypeFactory {

    override fun type(data: CourierViewData): Int {
        return CourierViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<*> {
        return if (type == CourierViewHolder.LAYOUT) {
            CourierViewHolder(parent)
        } else {
            super.createViewHolder(parent, type)
        }
    }
}
