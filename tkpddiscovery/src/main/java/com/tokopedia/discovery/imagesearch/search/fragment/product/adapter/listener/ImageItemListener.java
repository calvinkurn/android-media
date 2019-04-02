package com.tokopedia.discovery.imagesearch.search.fragment.product.adapter.listener;

import com.tokopedia.discovery.newdiscovery.base.EmptyStateListener;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.viewmodel.ProductItem;

/**
 * Created by sachinbansal on 4/13/18.
 */

public interface ImageItemListener extends EmptyStateListener {

    void onItemClicked(ProductItem item, int adapterPosition);

    void onWishListButtonClicked(ProductItem productItem, int adapterPosition);
}
