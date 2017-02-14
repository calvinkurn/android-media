package com.tokopedia.seller.topads.view.listener;

import android.support.annotation.NonNull;

import com.tokopedia.core.shopinfo.models.shopmodel.ShopModel;
import com.tokopedia.seller.topads.model.data.DataDeposit;
import com.tokopedia.seller.topads.model.data.Summary;
import com.tokopedia.seller.topads.model.data.TotalAd;
import com.tokopedia.seller.topads.presenter.TopAdsDashboardPresenter;

/**
 * Created by Nathaniel on 11/24/2016.
 */

public interface TopAdsDashboardProductFragmentListener extends TopAdsDashboardFragmentListener{

    void onTotalAdLoaded(@NonNull TotalAd totalAd);

    void onLoadTotalAdError(@NonNull Throwable throwable);
}
