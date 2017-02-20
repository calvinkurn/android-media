package com.tokopedia.seller.topads.view.listener;

import com.tokopedia.seller.topads.domain.model.data.Ad;

/**
 * Created by zulfikarrahman on 12/30/16.
 */
public interface TopAdsDetailViewListener {

    void onAdLoaded(Ad ad);

    void onLoadAdError();

    void onTurnOnAdSuccess();

    void onTurnOnAdError();

    void onTurnOffAdSuccess();

    void onTurnOffAdError();

    void onDeleteAdSuccess();

    void onDeleteAdError();
}
