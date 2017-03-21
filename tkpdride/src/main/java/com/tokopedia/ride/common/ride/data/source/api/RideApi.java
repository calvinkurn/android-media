package com.tokopedia.ride.common.ride.data.source.api;

import com.tokopedia.ride.common.ride.data.entity.FareEstimateEntity;
import com.tokopedia.ride.common.ride.data.entity.ProductResponseEntity;
import com.tokopedia.ride.common.ride.data.entity.TimesEstimateEntity;
import com.tokopedia.ride.common.ride.data.entity.TimesEstimateResponseEntity;

import java.util.Map;

import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by alvarisi on 3/16/17.
 */

public interface RideApi {
    @GET(RideUrl.PRODUCTS)
    Observable<ProductResponseEntity> getProducts(@QueryMap Map<String, Object> param);

    @GET(RideUrl.ESTIMATED_TIME)
    Observable<TimesEstimateResponseEntity> getEstimateds(@QueryMap Map<String, Object> param);

    @GET(RideUrl.ESTIMATED_FARE)
    Observable<FareEstimateEntity> getFareEstimateds(@QueryMap Map<String, Object> param);
}
