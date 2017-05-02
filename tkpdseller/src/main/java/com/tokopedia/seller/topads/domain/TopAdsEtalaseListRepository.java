package com.tokopedia.seller.topads.domain;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.seller.topads.data.model.data.Etalase;
import com.tokopedia.seller.topads.data.model.data.GroupAd;

import java.util.List;

import rx.Observable;

/**
 * Created by Hendry on 2/20/17.
 */
public interface TopAdsEtalaseListRepository {
    Observable<List<Etalase>> getEtalaseList(String shopId);
}
