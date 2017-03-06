package com.tokopedia.seller.topads.view.listener;

import android.support.annotation.NonNull;

import com.tokopedia.seller.topads.data.model.data.Ad;

import java.util.List;

/**
 * Created by zulfikarrahman on 11/24/16.
 */
public interface TopAdsListPromoViewListener<T extends Ad> {

    void onSearchAdLoaded(@NonNull List<T> adList, int totalItem);

    void onLoadSearchAdError();

}