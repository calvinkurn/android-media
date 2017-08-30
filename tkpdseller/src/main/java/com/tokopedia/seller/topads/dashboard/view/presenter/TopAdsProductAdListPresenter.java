package com.tokopedia.seller.topads.dashboard.view.presenter;

import com.tokopedia.seller.topads.dashboard.data.model.data.ProductAd;

import java.util.Date;

/**
 * Created by zulfikarrahman on 12/16/16.
 */
public interface TopAdsProductAdListPresenter extends TopAdsAdListPresenter<ProductAd> {

    void searchAd(Date startDate, Date endDate, String keyword, int status, long groupId, int page);
}