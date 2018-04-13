package com.tokopedia.discovery.imagesearch.search.fragment.product;

import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.ProductListFragmentView;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.listener.WishlistActionListener;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.viewmodel.ProductItem;
import com.tokopedia.discovery.newdiscovery.util.SearchParameter;

import java.util.HashMap;

/**
 * Created by sachinbansal on 4/13/18.
 */

public interface ImageProductListPresenter extends CustomerPresenter<ImageProductListFragmentView> {

    void loadMoreData(SearchParameter searchParameter, HashMap<String, String> additionalParams);

    void loadData(SearchParameter searchParameter, boolean isForceSearch, HashMap<String, String> additionalParams);

    void handleWishlistButtonClicked(ProductItem productItem, int adapterPosition);

    void attachView(ImageProductListFragmentView viewListener, WishlistActionListener wishlistActionListener);

}
