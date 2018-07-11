package com.tokopedia.discovery.newdiscovery.search.fragment.product.adapter.viewholder;

import android.support.annotation.LayoutRes;
import android.view.View;

import com.tokopedia.discovery.R;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.adapter.listener.ItemClickListener;

public class BigGridProductItemViewHolder extends GridProductItemViewHolder {
    @LayoutRes
    public static final int LAYOUT = R.layout.search_result_product_item_big_grid;

    public BigGridProductItemViewHolder(View itemView, ItemClickListener itemClickListener) {
        super(itemView, itemClickListener);
    }
}
