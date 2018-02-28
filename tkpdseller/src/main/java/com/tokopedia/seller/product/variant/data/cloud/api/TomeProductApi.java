package com.tokopedia.seller.product.variant.data.cloud.api;

import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.seller.product.edit.constant.ProductUrl;
import com.tokopedia.seller.product.edit.data.source.cloud.model.ProductUploadResultModel;
import com.tokopedia.seller.product.edit.view.model.edit.ProductViewModel;
import com.tokopedia.seller.product.variant.data.model.variantbycat.ProductVariantByCatModel;
import com.tokopedia.seller.product.variant.data.model.variantbyprdold.ProductVariantByPrdModel;

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

    //TODO data response will be changed. ProductUploadResultModel no longer valid
    @Headers({"Content-Type: application/json"})
    @POST(ProductUrl.URL_ADD_PRODUCT)
    Observable<Response<DataResponse<ProductUploadResultModel>>> addProductSubmit(@Body String productViewModel);

    //TODO data response will be changed. ProductUploadResultModel no longer valid
    @Headers({"Content-Type: application/json"})
    @PATCH(ProductUrl.URL_ADD_PRODUCT + "/{" + ProductUrl.PRODUCT_ID + "}")
    Observable<Response<DataResponse<ProductUploadResultModel>>> editProductSubmit(@Path(ProductUrl.PRODUCT_ID) String productId, @Body String productViewModel);

    @GET(ProductUrl.URL_ADD_PRODUCT + "/{" + ProductUrl.PRODUCT_ID + "}")
    Observable<Response<DataResponse<ProductViewModel>>> getProductDetail(
            @Path(ProductUrl.PRODUCT_ID) String productId,
            @Query("show_variant") int showVariant);

    @GET(ProductUrl.GET_VARIANT_BY_CAT_PATH)
    Observable<Response<DataResponse<List<ProductVariantByCatModel>>>> getProductVariantByCat(@Query(ProductUrl.CAT_ID) long categoryId);

    @GET(ProductUrl.GET_VARIANT_BY_PRD_PATH)
    Observable<Response<DataResponse<ProductVariantByPrdModel>>> getProductVariantByPrd(@Query(ProductUrl.PRD_ID) long productId);

}
