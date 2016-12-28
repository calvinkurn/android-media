package com.tokopedia.seller.topads.presenter;

import java.util.Date;
import java.util.List;

/**
 * Created by zulfikarrahman on 11/24/16.
 */
public interface TopAdsAdListPresenter<T> {
    void getListTopAdsFromNet(Date startDate, Date endDate);

    void actionDeleteAds(List<T> ads);

    void actionOffAds(List<T> ads);

    void actionOnAds(List<T> ads);

    void loadMore(Date startDate, Date endDate, int lastItemPosition, int visibleItem);
}
