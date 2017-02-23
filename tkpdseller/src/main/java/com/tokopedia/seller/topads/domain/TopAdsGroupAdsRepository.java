package com.tokopedia.seller.topads.domain;

import com.tokopedia.seller.topads.domain.model.data.GroupAd;

import java.util.List;

import rx.Observable;

/**
 * Created by zulfikarrahman on 2/20/17.
 */
public interface TopAdsGroupAdsRepository {
    Observable<List<GroupAd>> searchGroupAds(String string);
}
