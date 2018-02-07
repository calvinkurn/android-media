package com.tokopedia.discovery.newdiscovery.search.fragment.shop.adapter.listener;

import com.tokopedia.discovery.newdiscovery.base.EmptyStateClickListener;
import com.tokopedia.discovery.newdiscovery.search.fragment.shop.viewmodel.ShopViewModel;

/**
 * Created by henrypriyono on 10/19/17.
 */

public interface ItemClickListener extends EmptyStateClickListener {
    void onItemClicked(ShopViewModel.ShopItem shopItem, int adapterPosition);

    void onFavoriteButtonClicked(ShopViewModel.ShopItem shopItem,
                                 int adapterPosition);
}
