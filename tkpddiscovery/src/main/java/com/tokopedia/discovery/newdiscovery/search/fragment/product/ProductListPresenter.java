package com.tokopedia.discovery.newdiscovery.search.fragment.product;

import com.tokopedia.discovery.newdiscovery.search.fragment.SearchSectionFragmentPresenter;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.listener.WishlistActionListener;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.viewmodel.ProductItem;
import com.tokopedia.discovery.newdiscovery.util.SearchParameter;

import java.util.HashMap;

/**
 * Created by henrypriyono on 10/11/17.
 */

public interface ProductListPresenter extends SearchSectionFragmentPresenter<ProductListFragmentView> {

    void loadMoreData(SearchParameter searchParameter, HashMap<String, String> additionalParams);

    void loadData(SearchParameter searchParameter, boolean isForceSearch, HashMap<String, String> additionalParams);

    void handleWishlistButtonClicked(ProductItem productItem, int adapterPosition);

    void attachView(ProductListFragmentView viewListener, WishlistActionListener wishlistActionListener);

    void loadGuidedSearch(String keyword);

    void requestQuickFilter(HashMap<String, String> paramMap);
}



