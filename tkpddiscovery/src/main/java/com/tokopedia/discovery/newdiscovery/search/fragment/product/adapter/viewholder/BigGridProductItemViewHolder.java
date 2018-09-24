package com.tokopedia.discovery.newdiscovery.search.fragment.product.adapter.viewholder;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.ImageView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.adapter.listener.ItemClickListener;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.viewmodel.ProductItem;

public class BigGridProductItemViewHolder extends GridProductItemViewHolder {
    @LayoutRes
    public static final int LAYOUT = R.layout.search_result_product_item_big_grid;

    public BigGridProductItemViewHolder(View itemView, ItemClickListener itemClickListener) {
        super(itemView, itemClickListener);
    }

    @Override
    public void setImageProduct(ProductItem productItem) {
        ImageHandler.loadImageSourceSize(context, productImage, productItem.getImageUrl700());
    }
}
