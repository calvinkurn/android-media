package com.tokopedia.seller.product.picker.data.api;

import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.seller.product.picker.data.model.ProductListSellerModel;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by zulfikarrahman on 9/4/17.
 */

public interface GetProductListSellerApi {
    @GET(TkpdBaseURL.Product.V4_PRODUCT + TkpdBaseURL.Product.PATH_MANAGE_PRODUCT)
    Observable<Response<ProductListSellerModel>> getProductList(@QueryMap Map<String, String> params);
}
