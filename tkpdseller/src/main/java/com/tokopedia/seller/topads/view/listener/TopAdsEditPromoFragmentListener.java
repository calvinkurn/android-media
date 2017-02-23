package com.tokopedia.seller.topads.view.listener;

import android.support.annotation.NonNull;

import com.tokopedia.seller.topads.view.model.AdDetailViewModel;

/**
 * Created by Nathaniel on 11/24/2016.
 */

public interface TopAdsEditPromoFragmentListener {

    void onAdDetailLoaded(@NonNull AdDetailViewModel adDetailViewModel);

    void onLoadAdDetailError();
}
