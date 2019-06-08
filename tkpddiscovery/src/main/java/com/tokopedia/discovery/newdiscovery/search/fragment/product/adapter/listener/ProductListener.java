package com.tokopedia.discovery.newdiscovery.search.fragment.product.adapter.listener;

import com.tokopedia.discovery.common.data.Option;
import com.tokopedia.discovery.newdiscovery.base.EmptyStateListener;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.viewmodel.GlobalNavViewModel;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.viewmodel.ProductItem;

/**
 * Created by henrypriyono on 10/18/17.
 */

public interface ProductListener extends EmptyStateListener {
    void onItemClicked(ProductItem item, int adapterPosition);

    void onLongClick(ProductItem item, int adapterPosition);

    void onWishlistButtonClicked(final ProductItem productItem);

    void onSuggestionClicked(String suggestedQuery);

    void onSearchGuideClicked(String queryParams);

    void onRelatedSearchClicked(String queryParams, String keyword);

    void onQuickFilterSelected(Option option);

    boolean isQuickFilterSelected(Option option);

    void onProductImpressed(ProductItem item, int adapterPosition);

    void onGlobalNavWidgetClicked(GlobalNavViewModel.Item item, String keyword);

    void onGlobalNavWidgetClickSeeAll(String applink, String url);
}
