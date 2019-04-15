package com.tokopedia.discovery.imagesearch.search.fragment.product;

import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.viewmodel.ProductItem;
import com.tokopedia.discovery.newdiscovery.search.model.SearchParameter;
import com.tokopedia.wishlist.common.listener.WishListActionListener;

import java.util.HashMap;

/**
 * Created by sachinbansal on 4/13/18.
 */

public interface ImageProductListPresenter extends CustomerPresenter<ImageProductListFragmentView> {

    void loadMoreData(SearchParameter searchParameter, HashMap<String, String> additionalParams);

    void handleWishlistButtonClicked(ProductItem productItem);

    void attachView(ImageProductListFragmentView viewListener, WishListActionListener wishlistActionListener);

}
