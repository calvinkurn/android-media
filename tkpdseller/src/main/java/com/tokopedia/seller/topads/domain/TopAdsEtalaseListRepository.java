package com.tokopedia.seller.topads.domain;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.seller.topads.domain.model.data.Etalase;
import com.tokopedia.seller.topads.domain.model.data.GroupAd;

import java.util.List;

import rx.Observable;

/**
 * Created by zulfikarrahman on 2/20/17.
 */
public interface TopAdsEtalaseListRepository {
    Observable<List<Etalase>> getEtalaseList(String shopId);
}
