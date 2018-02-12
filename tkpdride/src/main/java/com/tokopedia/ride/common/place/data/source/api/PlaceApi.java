package com.tokopedia.ride.common.place.data.source.api;

import com.google.gson.JsonObject;
import com.tokopedia.ride.common.place.data.entity.DirectionEntity;
import com.tokopedia.ride.common.place.data.entity.DistanceMatrixEntity;

import java.util.Map;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by alvarisi on 3/18/17.
 */

public interface PlaceApi {
    @GET("/maps/api/directions/{output}")
    Observable<DirectionEntity> getRoute(@Path("output") String output, @QueryMap Map<String, Object> params);

    @GET("/maps/api/distancematrix/{output}")
    Observable<DistanceMatrixEntity> getDistanceMatrix(@Path("output") String output, @QueryMap Map<String, Object> params);

    //@GET("https://maps.googleapis.com/maps/api/geocode/json?key=AIzaSyCRkgwGBe8ZxjcK07Cnl3Auf72BpgA6lLo")
    @GET("https://maps.googleapis.com/maps/api/geocode/json")
    Observable<JsonObject> getAddressFromGoogleAPI(@Query("key") String key, @Query("address") String address);
}