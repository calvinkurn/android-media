package com.tokopedia.discovery.newdiscovery.search.fragment.product;

import com.tokopedia.discovery.newdiscovery.search.fragment.SearchSectionFragmentPresenter;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.viewmodel.ProductItem;
import com.tokopedia.discovery.newdiscovery.util.SearchParameter;
import com.tokopedia.wishlist.common.listener.WishListActionListener;

import java.util.HashMap;

/**
 * Created by henrypriyono on 10/11/17.
 */

public interface ProductListPresenter extends SearchSectionFragmentPresenter<ProductListFragmentView> {

    void loadMoreData(SearchParameter searchParameter, HashMap<String, String> additionalParams);

    void loadData(SearchParameter searchParameter, boolean isForceSearch, HashMap<String, String> additionalParams);

    void handleWishlistButtonClicked(ProductItem productItem);

    void attachView(ProductListFragmentView viewListener, WishListActionListener wishlistActionListener);

    void loadGuidedSearch(String keyword);

    void requestQuickFilter(HashMap<String, String> paramMap);

    void setIsUsingFilterV4(boolean isUsingFilterV4);
}



