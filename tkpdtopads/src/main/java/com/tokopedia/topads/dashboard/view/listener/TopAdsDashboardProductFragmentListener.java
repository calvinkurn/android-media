package com.tokopedia.topads.dashboard.view.listener;

import android.support.annotation.NonNull;

import com.tokopedia.topads.dashboard.data.model.data.TotalAd;

/**
 * Created by Nathaniel on 11/24/2016.
 */

public interface TopAdsDashboardProductFragmentListener extends TopAdsDashboardFragmentListener{

    void onTotalAdLoaded(@NonNull TotalAd totalAd);

    void onLoadTotalAdError(@NonNull Throwable throwable);
}
