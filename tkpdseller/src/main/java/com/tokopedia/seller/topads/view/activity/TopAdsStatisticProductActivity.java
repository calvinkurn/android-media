package com.tokopedia.seller.topads.view.activity;

import android.app.Activity;
import android.os.Bundle;

import com.tokopedia.seller.topads.constant.TopAdsNetworkConstant;

public class TopAdsStatisticProductActivity extends TopAdsStatisticActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getTypeStatistic() {
        return TopAdsNetworkConstant.TYPE_PRODUCT_STAT;
    }
}
