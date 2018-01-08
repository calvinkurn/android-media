package com.tokopedia.ride.common.place.data;

import com.google.gson.JsonObject;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.ride.common.place.data.entity.DirectionEntity;
import com.tokopedia.ride.common.place.data.entity.DistanceMatrixEntity;

import rx.Observable;

/**
 * Created by alvarisi on 3/18/17.
 */

public interface PlaceDataStore {
    Observable<DirectionEntity> getDirection(String output, TKPDMapParam<String, Object> param);

    Observable<DistanceMatrixEntity> getDistanceMarix(String output, TKPDMapParam<String, Object> param);

    Observable<JsonObject> getAddressFromGoogleAPI(String key, String address);

}
