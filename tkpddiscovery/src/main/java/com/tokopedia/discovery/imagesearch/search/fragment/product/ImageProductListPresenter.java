package com.tokopedia.discovery.imagesearch.search.fragment.product;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.viewmodel.ProductItem;
import com.tokopedia.discovery.newdiscovery.search.model.SearchParameter;
import com.tokopedia.wishlist.common.listener.WishListActionListener;

import java.util.HashMap;
import java.util.List;

/**
 * Created by sachinbansal on 4/13/18.
 */

public interface ImageProductListPresenter extends CustomerPresenter<ImageProductListFragmentView> {

    void handleWishlistButtonClicked(ProductItem productItem);

    void attachView(ImageProductListFragmentView viewListener, WishListActionListener wishlistActionListener);

    void initData(List<Visitable> data);

    void loadMoreData(int page);
}
