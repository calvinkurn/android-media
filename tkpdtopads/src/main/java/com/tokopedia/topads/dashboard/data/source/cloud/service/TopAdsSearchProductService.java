package com.tokopedia.topads.dashboard.data.source.cloud.service;


import com.tokopedia.topads.dashboard.data.model.data.Product;
import com.tokopedia.seller.common.data.response.DataResponse;

import java.util.List;
import java.util.Map;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * @author normansyahputa on 2/17/17.
 */

public interface TopAdsSearchProductService {

    @GET("/v1.1/dashboard/search_products")
    Observable<Response<DataResponse<List<Product>>>> searchProduct(
            @QueryMap Map<String, String> params
    );
}
