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
    BrowseProductModel getDataForBrowseProduct(boolean firstTimeOnly);
    NetworkParam.Product getProductParam();
    boolean checkHasFilterAttrIsNull(int activeTab);
}
