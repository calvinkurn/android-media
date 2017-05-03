package com.tokopedia.seller.topads.view.presenter;

import java.util.Date;

/**
 * Created by zulfikarrahman on 12/29/16.
 */
public interface TopAdsDetailPresenter extends RetrofitPresenter {

    void refreshAd(Date startDate, Date endDate, String id);

    void turnOnAds(String id);

    void turnOffAds(String id);

    void deleteAd(String id);
}
