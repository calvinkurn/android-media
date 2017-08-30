package com.tokopedia.seller.topads.dashboard.view.activity;

import android.os.Bundle;

import com.tokopedia.seller.topads.dashboard.constant.TopAdsNetworkConstant;

public class TopAdsStatisticShopActivity extends TopAdsStatisticActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getTypeStatistic() {
        return TopAdsNetworkConstant.TYPE_PRODUCT_SHOP;
    }
}
