package com.tokopedia.seller.topads.presenter;

import com.tokopedia.seller.topads.model.data.ProductAd;

import java.util.Date;

/**
 * Created by zulfikarrahman on 12/16/16.
 */
public interface TopAdsProductAdListPresenter extends TopAdsAdListPresenter<ProductAd> {

    void searchAd(Date startDate, Date endDate, String keyword, int status, int group, int page);
}