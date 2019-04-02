package com.tokopedia.tkpdpdp.courier

import android.support.annotation.LayoutRes
import android.text.TextUtils
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.tkpdpdp.R
import kotlinx.android.synthetic.main.courier_item.view.*

class CourierViewHolder(parent: View) : AbstractViewHolder<CourierViewData>(parent) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.courier_item
        const val CONSTANT_DELIMITER_COMMA = ", "
    }

    override fun bind(element: CourierViewData) {
        itemView.courier_item_name.text = element.courierName
        itemView.courier_item_info.text = TextUtils.join(
                CONSTANT_DELIMITER_COMMA,
                element.packageName
        )
        ImageHandler.LoadImage(itemView.courier_item_image, element.logo)
    }

}
