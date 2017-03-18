package com.tokopedia.ride.common.place.domain;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.ride.common.place.domain.model.OverviewPolyline;

import rx.Observable;

/**
 * Created by alvarisi on 3/18/17.
 */

public interface PlaceRepository {
    Observable<OverviewPolyline> getOveriewPolyline(TKPDMapParam<String, Object> param);
}
