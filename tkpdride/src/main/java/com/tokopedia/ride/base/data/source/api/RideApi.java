package com.tokopedia.ride.base.data.source.api;

import com.tokopedia.ride.base.data.entity.ProductEntity;
import com.tokopedia.ride.base.data.entity.ProductResponseEntity;

import java.util.List;
import java.util.Map;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by alvarisi on 3/16/17.
 */

public interface RideApi {
    @GET(RideUrl.PRODUCTS)
    Observable<ProductResponseEntity> getProducts(@QueryMap Map<String, Object> param);
}
