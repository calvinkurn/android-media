package com.tokopedia.seller.topads.presenter;

import com.tokopedia.core.var.RecyclerViewItem;

import java.util.List;

/**
 * Created by zulfikarrahman on 11/24/16.
 */
public interface TopAdsListPresenter {
    List<RecyclerViewItem> getListTopAds();

    void getListTopAdsFromNet();
}
