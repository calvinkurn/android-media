package com.tokopedia.discovery.imagesearch.search.fragment.product;

import android.os.Bundle;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.discovery.newdiscovery.search.model.SearchParameter;

import java.util.List;

/**
 * Created by sachinbansal on 4/13/18.
 */

public interface ImageProductListFragmentView extends CustomerView {

    void launchLoginActivity(Bundle extras);

    boolean isUserHasLogin();

    String getUserId();

    void initTopAdsParams();

    void incrementStart();

    boolean isEvenPage();

    void storeTotalData(int totalData);

    int getStartFrom();

    void setTopAdsEndlessListener();

    void unSetTopAdsEndlessListener();

    void setHeaderTopAds(boolean hasHeader);

    void setProductList(List<Visitable> list);

    void disableWishlistButton(String productId);

    void enableWishlistButton(String productId);

    void showNetworkError(int startRow);

    String getQueryKey();

    void setEmptyProduct();

    SearchParameter getSearchParameter();

    void setSearchParameter(SearchParameter searchParameter);

    void backToTop();

    void hideRefreshLayout();
}
