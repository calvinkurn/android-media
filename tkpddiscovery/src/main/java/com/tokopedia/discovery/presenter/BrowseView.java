package com.tokopedia.discovery.presenter;

import com.tokopedia.core.BuildConfig;
import com.tokopedia.core.network.entity.discovery.BrowseProductActivityModel;
import com.tokopedia.core.network.entity.discovery.BrowseProductModel;
import com.tokopedia.discovery.model.NetworkParam;

/**
 * Created by noiz354 on 3/24/16.
 */
public interface BrowseView {
    BrowseProductActivityModel getBrowseProductActivityModel();
    boolean checkHasFilterAttrIsNull(int activeTab);

    String TAG = "DiscoveryActivity";
    String SEARCH_ACTION_INTENT = BuildConfig.APPLICATION_ID+".SEARCH";
    String CHANGE_GRID_ACTION_INTENT = BuildConfig.APPLICATION_ID+".LAYOUT";
    String GRID_TYPE_EXTRA = "GRID_TYPE_EXTRA";
    int REQUEST_SORT = 121;


    BrowseProductModel getDataForBrowseProduct(boolean firstTimeOnly);

    NetworkParam.Product getProductParam();
}
