package com.tokopedia.seller.topads.view.presenter;

import com.tokopedia.seller.topads.data.model.data.GroupAd;

import java.util.Date;

/**
 * Created by zulfikarrahman on 12/22/16.
 */

public interface TopAdsGroupAdListPresenter extends TopAdsAdListPresenter<GroupAd> {

    void searchAd(Date startDate, Date endDate, String keyword, int status, int page);

}