package com.tokopedia.seller.topads.presenter;

import com.tokopedia.seller.topads.model.data.Ad;

import java.util.List;

/**
 * Created by zulfikarrahman on 11/24/16.
 */
public interface TopAdsAdListPresenter<T extends Ad> {

    void turnOffAdList(List<T> adList);

    void turnOnAddList(List<T> adList);
}