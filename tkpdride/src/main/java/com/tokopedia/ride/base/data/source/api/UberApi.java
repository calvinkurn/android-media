package com.tokopedia.ride.base.data.source.api;

import com.tokopedia.ride.base.data.entity.ProductEntity;

import java.util.List;
import java.util.Map;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by alvarisi on 3/14/17.
 */

public interface UberApi {
    @GET(UberUrl.PRODUCTS)
    Observable<List<ProductEntity>> getProducts(@QueryMap Map<String, Object> param);

    @GET(UberUrl.PRODUCTS + "/{product_id}")
    Observable<String> getProduct(@Path("product_id") String productId);
}
