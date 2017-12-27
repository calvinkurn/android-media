package com.tokopedia.topads.dashboard.view.listener;

import android.support.annotation.NonNull;

import com.tokopedia.topads.dashboard.data.model.data.ShopAd;

/**
 * Created by Nathaniel on 11/24/2016.
 */

public interface TopAdsDashboardStoreFragmentListener extends TopAdsDashboardFragmentListener{

    void onAdShopLoaded(@NonNull ShopAd shopAd);

    void onLoadAdShopError();
}
