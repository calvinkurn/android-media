package com.tokopedia.discovery.newdiscovery.search.fragment.product;

import android.os.Bundle;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.discovery.model.DataValue;
import com.tokopedia.core.discovery.model.Filter;
import com.tokopedia.core.discovery.model.Option;
import com.tokopedia.discovery.newdiscovery.search.fragment.SearchSectionFragmentView;
import com.tokopedia.discovery.newdiscovery.search.model.SearchParameter;

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

    List<Option> getQuickFilterOptions(DataValue dynamicFilterModel);

    void addLoading();

    void removeLoading();

    void onSuccessAddWishlist(String productId);

    void onErrorAddWishList(String errorMessage, String productId);

    void notifyAdapter();

    void stopTracePerformanceMonitoring();

    void initQuickFilter(List<Filter> quickFilterList);
}
