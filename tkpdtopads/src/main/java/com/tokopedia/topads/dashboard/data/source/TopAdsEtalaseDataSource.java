package com.tokopedia.topads.dashboard.data.source;

import com.tokopedia.topads.dashboard.data.model.data.Etalase;

import java.util.List;

import rx.Observable;

/**
 * @author hendry on 2/24/17.
 */

public interface TopAdsEtalaseDataSource {
    Observable<List<Etalase>> getEtalaseList(String shopId);
}
