package com.tokopedia.seller.manageitem.data.cloud.api;


import com.tokopedia.network.data.model.response.DataResponse;
import com.tokopedia.seller.manageitem.common.util.ProductUrl;
import com.tokopedia.seller.manageitem.data.cloud.model.product.ProductSubmitResp;
import com.tokopedia.seller.manageitem.data.cloud.model.product.ProductVariantByCatModel;
import com.tokopedia.seller.manageitem.data.cloud.model.product.ProductVariantByPrdModel;
import com.tokopedia.seller.manageitem.data.cloud.model.product.ProductViewModel;

import java.util.List;

import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by hendry on 24/05/17.
 */

public interface TomeProductApi {

    @Headers({"Content-Type: application/json"})
    @POST(ProductUrl.URL_ADD_PRODUCT)
    Observable<Response<DataResponse<ProductSubmitResp>>> addProductSubmit(@Body String productViewModel);

    @Headers({"Content-Type: application/json"})
    @PATCH(ProductUrl.URL_ADD_PRODUCT + "/{" + ProductUrl.PRODUCT_ID + "}")
    Observable<Response<DataResponse<ProductSubmitResp>>> editProductSubmit(@Path(ProductUrl.PRODUCT_ID) String productId, @Body String productViewModel);

    @GET(ProductUrl.URL_ADD_PRODUCT + "/{" + ProductUrl.PRODUCT_ID + "}")
    Observable<Response<DataResponse<ProductViewModel>>> getProductDetail(@Path(ProductUrl.PRODUCT_ID) String productId, @Query("show_variant") int showVariant, @Query("use_real_stock") boolean useRealStock);

    @GET(ProductUrl.GET_VARIANT_BY_CAT_PATH)
    Observable<Response<DataResponse<List<ProductVariantByCatModel>>>> getProductVariantByCat(@Query(ProductUrl.CAT_ID) long categoryId);

    @GET(ProductUrl.GET_VARIANT_BY_PRD_PATH)
    Observable<Response<DataResponse<ProductVariantByPrdModel>>> getProductVariantByPrd(@Query(ProductUrl.PRD_ID) long productId);

}
