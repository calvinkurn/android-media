package com.tokopedia.seller.topads.presenter;

import com.tokopedia.seller.topads.model.data.Ad;
import com.tokopedia.seller.topads.model.data.ProductAd;

/**
 * Created by zulfikarrahman on 12/29/16.
 */
public interface TopAdsDetailPresenter {
    void turnOnAds(Ad ad, String shopId);

    void turnOffAds(Ad ad, String shopId);
}
