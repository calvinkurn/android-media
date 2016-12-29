package com.tokopedia.seller.topads.presenter;

import com.tokopedia.seller.topads.model.data.GroupAd;

import java.util.Date;

/**
 * Created by zulfikarrahman on 12/22/16.
 */

public interface TopAdsGroupAdListPresenter extends TopAdsAdListPresenter<GroupAd> {

    void searchAd(Date startDate, Date endDate, String keyword, int status, int page);
}
