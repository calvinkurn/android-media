package com.tokopedia.discovery.newdiscovery.search.fragment.product.adapter.listener;

import com.tokopedia.discovery.newdiscovery.base.EmptyStateClickListener;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.viewmodel.ProductItem;

/**
 * Created by henrypriyono on 10/18/17.
 */

public interface ItemClickListener extends EmptyStateClickListener {
    void onItemClicked(ProductItem item, int adapterPosition);

    void onWishlistButtonClicked(ProductItem productItem, int adapterPosition);

    void onSuggestionClicked(String suggestedQuery);
}
