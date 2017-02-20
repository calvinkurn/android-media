package com.tokopedia.seller.topads.view.presenter;

import java.util.Date;

/**
 * Created by zulfikarrahman on 12/29/16.
 */
public interface TopAdsDetailPresenter extends RetrofitPresenter {

    void refreshAd(Date startDate, Date endDate, int id);

    void turnOnAds(int id);

    void turnOffAds(int id);

    void deleteAd(int id);
}
