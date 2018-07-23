package com.tokopedia.discovery.newdiscovery.search.fragment.product.adapter.listener;

import android.text.TextUtils;

import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.discovery.model.Option;
import com.tokopedia.discovery.newdiscovery.base.EmptyStateClickListener;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.viewmodel.ProductItem;

/**
 * Created by henrypriyono on 10/18/17.
 */

public interface ItemClickListener extends EmptyStateClickListener {
    void onItemClicked(ProductItem item, int adapterPosition);

    void onWishlistButtonClicked(ProductItem productItem);

    void onSuggestionClicked(String suggestedQuery);

    void onBannerAdsClicked(String appLink);

    void onSearchGuideClicked(String keyword);

    void onQuickFilterSelected(Option option);

    void onSelectedFilterRemoved(String uniqueId);
}
