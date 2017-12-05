package com.tokopedia.discovery.newdiscovery.category.presentation.product.adapter.listener;


import com.tokopedia.discovery.newdiscovery.category.presentation.product.viewmodel.ProductItem;

/**
 * Created by henrypriyono on 10/18/17.
 */

public interface ItemClickListener {
    void onItemClicked(ProductItem item, int adapterPosition);

    void onWishlistButtonClicked(ProductItem productItem, int adapterPosition);
}
