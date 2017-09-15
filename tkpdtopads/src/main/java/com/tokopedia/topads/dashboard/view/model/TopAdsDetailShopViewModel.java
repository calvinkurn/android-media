package com.tokopedia.topads.dashboard.view.model;

import com.tokopedia.topads.dashboard.constant.TopAdsNetworkConstant;

/**
 * Created by Nathaniel on 2/23/2017.
 */

public class TopAdsDetailShopViewModel extends TopAdsDetailProductViewModel {

    @Override
    public int getType() {
        return TopAdsNetworkConstant.TYPE_PRODUCT_SHOP;
    }

    @Override
    public long getItemId() {
        return getShopId();
    }
}
