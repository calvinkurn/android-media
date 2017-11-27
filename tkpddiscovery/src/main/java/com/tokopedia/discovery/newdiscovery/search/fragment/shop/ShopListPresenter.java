package com.tokopedia.discovery.newdiscovery.search.fragment.shop;

import com.tokopedia.discovery.newdiscovery.search.fragment.SearchSectionFragmentPresenter;
import com.tokopedia.discovery.newdiscovery.search.fragment.shop.listener.FavoriteActionListener;
import com.tokopedia.discovery.newdiscovery.search.fragment.shop.viewmodel.ShopViewModel;
import com.tokopedia.discovery.newdiscovery.util.SearchParameter;

/**
 * Created by henrypriyono on 10/13/17.
 */

public interface ShopListPresenter extends SearchSectionFragmentPresenter<ShopListFragmentView> {
    void attachView(ShopListFragmentView viewListener,
                    FavoriteActionListener favoriteActionListener);

    void loadShop(SearchParameter searchParameter,
                  ShopListPresenterImpl.LoadMoreListener loadMoreListener);

    void handleFavoriteButtonClicked(ShopViewModel.ShopItem shopItem,
                                     int adapterPosition);
}
