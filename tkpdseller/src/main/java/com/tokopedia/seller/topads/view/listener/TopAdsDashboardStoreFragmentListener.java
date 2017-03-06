package com.tokopedia.seller.topads.view.listener;

import android.support.annotation.NonNull;

import com.tokopedia.seller.topads.data.model.data.ShopAd;

/**
 * Created by Nathaniel on 11/24/2016.
 */

public interface TopAdsDashboardStoreFragmentListener extends TopAdsDashboardFragmentListener{

    void onAdShopLoaded(@NonNull ShopAd shopAd);

    void onLoadAdShopError();
}
