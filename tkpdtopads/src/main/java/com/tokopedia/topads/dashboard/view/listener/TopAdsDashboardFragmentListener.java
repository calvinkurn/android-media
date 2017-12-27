package com.tokopedia.topads.dashboard.view.listener;

import android.support.annotation.NonNull;

import com.tokopedia.core.shopinfo.models.shopmodel.ShopModel;
import com.tokopedia.seller.common.topads.deposit.data.model.DataDeposit;
import com.tokopedia.topads.dashboard.data.model.data.Summary;

/**
 * Created by Nathaniel on 11/24/2016.
 */

public interface TopAdsDashboardFragmentListener {

    void onSummaryLoaded(@NonNull Summary summary);

    void onLoadSummaryError(@NonNull Throwable throwable);

    void onDepositTopAdsLoaded(@NonNull DataDeposit dataDeposit);

    void onLoadDepositTopAdsError(@NonNull Throwable throwable);

    void onShopDetailLoaded(@NonNull ShopModel shopModel);

    void onLoadShopDetailError(@NonNull Throwable throwable);
}
