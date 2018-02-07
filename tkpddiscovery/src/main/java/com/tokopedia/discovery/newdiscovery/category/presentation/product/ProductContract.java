package com.tokopedia.discovery.newdiscovery.category.presentation.product;

import android.os.Bundle;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.discovery.newdiscovery.category.presentation.product.listener.WishlistActionListener;
import com.tokopedia.discovery.newdiscovery.category.presentation.product.viewmodel.CategoryHeaderModel;
import com.tokopedia.discovery.newdiscovery.category.presentation.product.viewmodel.ProductItem;
import com.tokopedia.discovery.newdiscovery.search.fragment.SearchSectionFragmentPresenter;
import com.tokopedia.discovery.newdiscovery.search.fragment.SearchSectionFragmentView;
import com.tokopedia.discovery.newdiscovery.util.SearchParameter;

import java.util.List;

/**
 * @author by alifa on 10/26/17.
 */

public class ProductContract {

    public interface View extends SearchSectionFragmentView {

        String getDepartmentId();

        void launchLoginActivity(Bundle extras);

        boolean isUserHasLogin();

        String getUserId();

        void initTopAdsParams();

        void disableWishlistButton(int adapterPosition);

        void enableWishlistButton(int adapterPosition);

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
        void handleWishlistButtonClicked(ProductItem productItem, int adapterPosition);
        void attachView(ProductContract.View viewListener, WishlistActionListener wishlistActionListener);

    }
}
