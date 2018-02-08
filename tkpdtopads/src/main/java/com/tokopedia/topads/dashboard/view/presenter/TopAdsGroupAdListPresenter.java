package com.tokopedia.topads.dashboard.view.presenter;

import com.tokopedia.topads.dashboard.data.model.data.GroupAd;

import java.util.Date;

/**
 * Created by zulfikarrahman on 12/22/16.
 */

public interface TopAdsGroupAdListPresenter extends TopAdsAdListPresenter<GroupAd> {

    void searchAd(Date startDate, Date endDate, String keyword, int status, int page);

}