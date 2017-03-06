package com.tokopedia.seller.topads.view.listener;

import android.support.annotation.NonNull;

import com.tokopedia.seller.topads.data.model.data.TotalAd;
import com.tokopedia.seller.topads.view.presenter.TopAdsDashboardPresenter;

/**
 * Created by Nathaniel on 11/24/2016.
 */

public interface TopAdsDashboardProductFragmentListener extends TopAdsDashboardFragmentListener{

    void onTotalAdLoaded(@NonNull TotalAd totalAd);

    void onLoadTotalAdError(@NonNull Throwable throwable);
}
