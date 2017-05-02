package com.tokopedia.seller.topads.view.listener;

import android.support.annotation.NonNull;

import com.tokopedia.seller.topads.view.model.TopAdsDetailAdViewModel;

/**
 * Created by Nathaniel on 11/24/2016.
 */

public interface TopAdsEditPromoFragmentListener {

    void onAdDetailLoaded(@NonNull TopAdsDetailAdViewModel topAdsAdDetailViewModel);

    void onLoadAdDetailError();
}
