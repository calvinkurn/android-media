package com.tokopedia.discovery.newdiscovery.search.fragment.product;

import android.os.Bundle;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.discovery.model.DynamicFilterModel;
import com.tokopedia.discovery.newdiscovery.search.fragment.SearchSectionFragmentView;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.viewmodel.GuidedSearchViewModel;
import com.tokopedia.discovery.newdiscovery.util.SearchParameter;

import java.util.List;

/**
 * Created by henrypriyono on 10/19/17.
 */

public interface ProductListFragmentView extends SearchSectionFragmentView {
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

    void onGetGuidedSearchComplete(GuidedSearchViewModel guidedSearchViewModel);

    void getQuickFilter();

    void renderQuickFilter(DynamicFilterModel dynamicFilterModel);

    void getGuidedSearch();
}
