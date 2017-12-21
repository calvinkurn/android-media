package com.tokopedia.discovery.newdiscovery.search.fragment.shop.adapter.viewholder;

import android.support.annotation.LayoutRes;
import android.view.View;

import com.tokopedia.discovery.R;
import com.tokopedia.discovery.newdiscovery.search.fragment.shop.adapter.listener.ItemClickListener;

/**
 * Created by henrypriyono on 10/17/17.
 */

public class ListShopItemViewHolder extends GridShopItemViewHolder {

    @LayoutRes
    public static final int LAYOUT = R.layout.layout_item_shop_list;

    public ListShopItemViewHolder(View itemView, ItemClickListener itemClickListener) {
        super(itemView, itemClickListener);
    }
}
