package com.tokopedia.discovery.view;

import android.support.v4.util.ArrayMap;

import com.drew.lang.annotations.Nullable;
import com.tokopedia.core.discovery.model.Breadcrumb;
import com.tokopedia.core.discovery.model.DataValue;
import com.tokopedia.core.network.entity.categoriesHades.Data;
import com.tokopedia.core.network.entity.discovery.BrowseProductModel;
import com.tokopedia.core.presenter.BaseView;
import com.tokopedia.discovery.model.NetworkParam;

import java.util.List;

/**
 * Created by Erry on 6/30/2016.
 */
public interface BrowseProductParentView extends BaseView {
    String MESSAGE_TAG = "BrowseProductParentView";
    String VISIBLE_ON = "1";
    String VISIBLE_OFF = "0";
    String BROWSE_PRODUCT_ACTIVITY_MODEL = "BROWSE_PRODUCT_ACTIVITY_MODEL";
    String IS_CATEGORY = "IS_CATEGORY";

    void initSectionAdapter(ArrayMap<String, String> visibleTab);
    void setupWithViewPager();
    void setupWithTabViewPager();
    void setNetworkStateError();
    void setLoadingProgress(boolean isLoading);
    void redirectUrl(BrowseProductModel productModel);
    void setupCategory(String departemenId);
    void renderCategories(Data a);
    void setDynamicFilterAtrribute(DataValue filterAtrribute, int activeTab);
    void setCurrentTabs(int pos);
    String getProductShareUrl();
    void setSource(String source);
    void initDiscoveryTicker();
    int getActiveTab();
    boolean checkHasFilterAttrIsNull(int activeTab);
    @Nullable
    List<Breadcrumb> getProductBreadCrumb();

    BrowseProductModel getDataForBrowseProduct(boolean firstTimeOnly);

    NetworkParam.Product getProductParam();
}
