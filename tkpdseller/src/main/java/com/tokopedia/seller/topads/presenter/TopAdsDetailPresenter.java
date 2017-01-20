package com.tokopedia.seller.topads.presenter;

import com.tokopedia.seller.topads.model.data.Ad;

import java.util.Date;

/**
 * Created by zulfikarrahman on 12/29/16.
 */
public interface TopAdsDetailPresenter extends RetrofitPresenter {

    void refreshAd(Date startDate, Date endDate, int id);

    void turnOnAds(Ad ad, String shopId);

    void turnOffAds(Ad ad, String shopId);
}
