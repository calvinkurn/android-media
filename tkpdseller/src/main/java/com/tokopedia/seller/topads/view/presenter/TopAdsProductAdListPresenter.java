package com.tokopedia.seller.topads.view.presenter;

import com.tokopedia.seller.topads.data.model.data.ProductAd;

import java.util.Date;

/**
 * Created by zulfikarrahman on 12/16/16.
 */
public interface TopAdsProductAdListPresenter extends TopAdsAdListPresenter<ProductAd> {

    void searchAd(Date startDate, Date endDate, String keyword, int status, int group, int page);
}