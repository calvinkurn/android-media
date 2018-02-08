package com.tokopedia.topads.dashboard.view.activity;

import android.os.Bundle;

import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.topads.dashboard.constant.TopAdsNetworkConstant;

public class TopAdsStatisticShopActivity extends TopAdsStatisticActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getTypeStatistic() {
        return TopAdsNetworkConstant.TYPE_PRODUCT_SHOP;
    }

    @Override
    protected void onCostSelected() {
        UnifyTracking.eventTopAdsShopStatisticBar(AppEventTracking.EventLabel.STATISTIC_OPTION_CPC);
    }

    @Override
    protected void onAverageConversionSelected() {
        UnifyTracking.eventTopAdsShopStatisticBar(AppEventTracking.EventLabel.STATISTIC_OPTION_AVERAGE_CONVERSION);
    }

    @Override
    protected void onConversionSelected() {
        UnifyTracking.eventTopAdsShopStatisticBar(AppEventTracking.EventLabel.STATISTIC_OPTION_CONVERSION);
    }

    @Override
    protected void onCtrSelected() {
        UnifyTracking.eventTopAdsShopStatisticBar(AppEventTracking.EventLabel.STATISTIC_OPTION_CTR);
    }

    @Override
    protected void onClickSelected() {
        UnifyTracking.eventTopAdsShopStatisticBar(AppEventTracking.EventLabel.STATISTIC_OPTION_CLICK);
    }

    @Override
    protected void onImpressionSelected() {
        UnifyTracking.eventTopAdsShopStatisticBar(AppEventTracking.EventLabel.STATISTIC_OPTION_IMPRESSION);
    }
}
