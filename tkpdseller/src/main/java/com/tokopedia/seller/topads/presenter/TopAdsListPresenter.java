package com.tokopedia.seller.topads.presenter;

import com.tokopedia.core.var.RecyclerViewItem;
import com.tokopedia.seller.topads.model.data.Ad;

import java.util.List;

/**
 * Created by zulfikarrahman on 11/24/16.
 */
public interface TopAdsListPresenter<T> {
    List<T> getListTopAds();

    void getListTopAdsFromNet();

    void actionDeleteAds(List<T> ads);

    void actionOffAds(List<T> ads);

    void actionOnAds(List<T> ads);

    void loadMore(int lastItemPosition, int visibleItem);
}
