package com.tokopedia.discovery.newdiscovery.search.fragment.product.adapter.listener;

import com.tokopedia.core.discovery.model.Option;
import com.tokopedia.discovery.newdiscovery.base.EmptyStateListener;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.viewmodel.ProductItem;

/**
 * Created by henrypriyono on 10/18/17.
 */

public interface ProductListener extends EmptyStateListener {
    void onItemClicked(ProductItem item, int adapterPosition);

    void onLongClick(ProductItem item, int adapterPosition);

    void onWishlistButtonClicked(final ProductItem productItem);

    void onSuggestionClicked(String suggestedQuery);

    void onSearchGuideClicked(String keyword);

    void onRelatedSearchClicked(String keyword);

    void onQuickFilterSelected(Option option);

    boolean isQuickFilterSelected(Option option);
}
