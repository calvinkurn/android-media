package com.tokopedia.seller.topads.data.source;

import com.tokopedia.seller.topads.domain.model.ProductDomain;
import com.tokopedia.seller.topads.data.model.data.Etalase;

import java.util.List;
import java.util.Map;

import rx.Observable;

/**
 * @author hendry on 2/24/17.
 */

public interface TopAdsEtalaseDataSource {
    Observable<List<Etalase>> getEtalaseList(String shopId);
}
