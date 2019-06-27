package com.tokopedia.discovery.newdiscovery.search.fragment.product.adapter.viewholder;

import android.support.annotation.LayoutRes;
import android.view.View;

import com.tokopedia.discovery.R;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.adapter.listener.ProductListener;

/**
 * Created by henrypriyono on 10/17/17.
 */

public class ListProductItemViewHolder extends GridProductItemViewHolder {

    @LayoutRes
    public static final int LAYOUT = R.layout.search_result_product_item_list;

    public ListProductItemViewHolder(View itemView, ProductListener itemClickListener, String searchQuery) {
        super(itemView, itemClickListener, searchQuery);
    }
}
