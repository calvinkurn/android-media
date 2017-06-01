package com.tokopedia.seller.topads.view.listener;

import android.support.annotation.NonNull;

import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.seller.topads.view.model.Ad;

import java.util.List;

/**
 * Created by zulfikarrahman on 11/24/16.
 */
public interface TopAdsListPromoViewListener<T extends Ad> extends CustomerView {

    void onSearchAdLoaded(@NonNull List<T> adList, int totalItem);

    void onLoadSearchAdError();

}