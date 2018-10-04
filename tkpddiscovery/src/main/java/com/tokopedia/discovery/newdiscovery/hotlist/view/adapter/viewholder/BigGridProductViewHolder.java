package com.tokopedia.discovery.newdiscovery.hotlist.view.adapter.viewholder;

import android.support.annotation.LayoutRes;
import android.view.View;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.newdiscovery.hotlist.view.adapter.ItemClickListener;
import com.tokopedia.discovery.newdiscovery.hotlist.view.model.HotlistProductViewModel;

public class BigGridProductViewHolder extends GridProductViewHolder {
    @LayoutRes
    public static final int LAYOUT = R.layout.search_result_product_item_big_grid;

    public BigGridProductViewHolder(View parent, ItemClickListener mItemClickListener) {
        super(parent, mItemClickListener);
    }

    @Override
    protected void renderProductImage(HotlistProductViewModel productItem) {
        ImageHandler.loadImageSourceSize(context, productImage, productItem.getImageUrl700());
    }
}
