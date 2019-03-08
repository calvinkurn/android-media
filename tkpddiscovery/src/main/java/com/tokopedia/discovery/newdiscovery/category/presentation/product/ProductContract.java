package com.tokopedia.discovery.newdiscovery.category.presentation.product;

import android.os.Bundle;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.discovery.newdiscovery.category.presentation.product.viewmodel.CategoryHeaderModel;
import com.tokopedia.discovery.newdiscovery.category.presentation.product.viewmodel.ProductItem;
import com.tokopedia.discovery.newdiscovery.search.fragment.SearchSectionFragmentPresenter;
import com.tokopedia.discovery.newdiscovery.search.fragment.SearchSectionFragmentView;
import com.tokopedia.discovery.newdiscovery.search.model.SearchParameter;
import com.tokopedia.wishlist.common.listener.WishListActionListener;

import java.util.List;
import java.util.Map;

/**
 * @author by alifa on 10/26/17.
 */

public class ProductContract {

    public interface View extends SearchSectionFragmentView {

        String getDepartmentId();

        void launchLoginActivity(Bundle extras);

        boolean isUserHasLogin();

        String getUserId();

        void trackEnhanceProduct(Map<String, Object> dataLayer);

        List<ProductItem> mappingTrackerProduct(List<ProductItem> productList, int page);

        void clearLastProductTracker(boolean clear);

        int getLastPositionProductTracker();

        void setLastPositionProductTracker(int lastPositionProductTracker);

        Map<String, Object> createImpressionProductDataLayer(List<ProductItem> productList);

        void initTopAdsParams();

        void disableWishlistButton(String productId);

        void enableWishlistButton(String productId);

        void showNetworkError(int startRow);

        void showRefreshLayout();

        void hideRefreshLayout();

        void setProductList(List<Visitable> productList);

        void showEmptyProduct();

        void setTopAdsEndlessListener();

        void unSetTopAdsEndlessListener();

        void backToTop();

    }

    public interface Presenter extends SearchSectionFragmentPresenter<View> {


        void loadDataProduct(SearchParameter searchParameter, CategoryHeaderModel categoryHeaderModel);

        void loadMore(SearchParameter searchParameter,
                      ProductPresenter.LoadMoreListener loadMoreListener);

        void handleWishlistButtonClicked(ProductItem productItem);

        void attachView(ProductContract.View viewListener, WishListActionListener wishlistActionListener);

    }
}
