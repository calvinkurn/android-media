package com.tokopedia.ride.common.place.data.source.api;

import com.tokopedia.ride.common.place.data.entity.DirectionEntity;

import java.util.Map;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by alvarisi on 3/18/17.
 */

public interface PlaceApi {
    @GET("/maps/api/directions/{output}")
    Observable<DirectionEntity> getRoute(@Path("output") String output, @QueryMap Map<String, Object> params);
}
