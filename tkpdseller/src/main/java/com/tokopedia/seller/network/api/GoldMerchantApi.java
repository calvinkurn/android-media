package com.tokopedia.seller.network.api;

import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by sebastianuskh on 2/2/17.
 */
public interface GoldMerchantApi {

    @GET("/v1/gold/product")
    Observable<String> getGoldMerchantProductList();
}
