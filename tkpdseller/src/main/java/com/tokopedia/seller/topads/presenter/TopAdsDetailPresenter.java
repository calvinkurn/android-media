package com.tokopedia.seller.topads.presenter;

import com.tokopedia.seller.topads.model.data.Ad;

/**
 * Created by zulfikarrahman on 12/29/16.
 */
public interface TopAdsDetailPresenter extends TopAdsDatePickerPresenter {

    void turnOnAds(Ad ad, String shopId);

    void turnOffAds(Ad ad, String shopId);
}
