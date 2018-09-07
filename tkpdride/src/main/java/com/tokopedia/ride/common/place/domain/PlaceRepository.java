package com.tokopedia.ride.common.place.domain;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.ride.common.place.data.entity.DistanceMatrixEntity;
import com.tokopedia.ride.common.place.data.entity.NearbyRoads;
import com.tokopedia.ride.common.place.data.entity.ReverseGeoCodeAddress;
import com.tokopedia.ride.common.place.domain.model.OverviewPolyline;

import java.util.List;

import rx.Observable;

/**
 * Created by alvarisi on 3/18/17.
 */

public interface PlaceRepository {
    Observable<List<OverviewPolyline>> getOveriewPolyline(TKPDMapParam<String, Object> param);

    Observable<DistanceMatrixEntity> getDistanceMatrix(TKPDMapParam<String, Object> param);

    Observable<ReverseGeoCodeAddress> getAddressFromGoogleAPI(String key, String address);

    Observable<NearbyRoads> getNearByRoadsFromGoogleAPI(String points, String key);

}
