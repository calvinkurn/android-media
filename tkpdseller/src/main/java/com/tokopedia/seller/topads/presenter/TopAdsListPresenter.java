package com.tokopedia.seller.topads.presenter;

import com.tokopedia.core.var.RecyclerViewItem;
import com.tokopedia.seller.topads.model.data.Ad;

import java.util.List;

/**
 * Created by zulfikarrahman on 11/24/16.
 */
public interface TopAdsListPresenter {
    List<Ad> getListTopAds();

    void getListTopAdsFromNet();
}
