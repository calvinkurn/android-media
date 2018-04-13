package com.tokopedia.ride.common.place.data.source;

import com.google.gson.JsonObject;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.ride.common.place.data.PlaceDataStore;
import com.tokopedia.ride.common.place.data.entity.DirectionEntity;
import com.tokopedia.ride.common.place.data.entity.DistanceMatrixEntity;
import com.tokopedia.ride.common.place.data.entity.NearbyRoads;
import com.tokopedia.ride.common.place.data.source.api.PlaceApi;

import rx.Observable;

/**
 * Created by alvarisi on 3/18/17.
 */

public class CloudPlaceDataStore implements PlaceDataStore {
    private final PlaceApi placeApi;

    public CloudPlaceDataStore(PlaceApi placeApi) {
        this.placeApi = placeApi;
    }

    @Override
    public Observable<DirectionEntity> getDirection(String output, TKPDMapParam<String, Object> param) {
        return this.placeApi.getRoute(output, param);
    }

    @Override
    public Observable<DistanceMatrixEntity> getDistanceMarix(String output, TKPDMapParam<String, Object> param) {
        return this.placeApi.getDistanceMatrix(output, param);
    }

    @Override
    public Observable<JsonObject> getAddressFromGoogleAPI(String key, String address) {
        return this.placeApi.getAddressFromGoogleAPI(key, address);
    }

    @Override
    public Observable<NearbyRoads> getNearbyRoadsFromGoogleAPI(String points, String key) {
        return this.placeApi.getNearbyRoadsFromGoogleAPI(points, key);
    }
}
