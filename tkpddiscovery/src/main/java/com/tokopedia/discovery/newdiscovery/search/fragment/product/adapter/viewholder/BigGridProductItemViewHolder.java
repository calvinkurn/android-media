package com.tokopedia.discovery.newdiscovery.search.fragment.product.adapter.viewholder;

import android.support.annotation.LayoutRes;
import android.view.View;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.adapter.listener.ProductListener;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.viewmodel.ProductItem;

public class BigGridProductItemViewHolder extends GridProductItemViewHolder {
    @LayoutRes
    public static final int LAYOUT = R.layout.search_result_product_item_big_grid;

    public BigGridProductItemViewHolder(View itemView, ProductListener itemClickListener, String searchQuery) {
        super(itemView, itemClickListener, searchQuery);
    }

    @Override
    public void setImageProduct(ProductItem productItem) {
        ImageHandler.loadImageSourceSize(context, productImage, productItem.getImageUrl700());
    }
}
