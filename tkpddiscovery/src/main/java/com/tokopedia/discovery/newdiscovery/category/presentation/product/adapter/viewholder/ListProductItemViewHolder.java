package com.tokopedia.discovery.newdiscovery.category.presentation.product.adapter.viewholder;

import android.support.annotation.LayoutRes;
import android.view.View;

import com.tokopedia.discovery.R;
import com.tokopedia.discovery.newdiscovery.category.presentation.product.adapter.listener.ItemClickListener;

/**
 * Created by henrypriyono on 10/17/17.
 */

public class ListProductItemViewHolder extends GridProductItemViewHolder {

    @LayoutRes
    public static final int LAYOUT = R.layout.listview_product_item_list;

    public ListProductItemViewHolder(View itemView, ItemClickListener itemClickListener) {
        super(itemView, itemClickListener);
    }
}
