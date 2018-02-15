package com.tokopedia.seller.product.edit.data.source.cloud.api;

import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.seller.product.edit.constant.ProductUrl;
import com.tokopedia.seller.product.edit.data.source.cloud.model.ProductUploadResultModel;
import com.tokopedia.seller.product.edit.view.model.edit.ProductViewModel;

import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

import static com.tokopedia.seller.product.edit.constant.ProductUrl.PRODUCT_ID;

/**
 * Created by zulfikarrahman on 2/5/18.
 */

public interface ProductApi {

    @Headers({"Content-Type: application/json"})
    @POST(ProductUrl.URL_ADD_PRODUCT)
    Observable<Response<DataResponse<ProductUploadResultModel>>> addProductSubmit(@Body ProductViewModel productViewModel);

    @Headers({"Content-Type: application/json"})
    @PATCH(ProductUrl.URL_ADD_PRODUCT + "/{" + PRODUCT_ID + "}")
    Observable<Response<DataResponse<ProductUploadResultModel>>> editProductSubmit(@Body ProductViewModel productViewModel,
                                                                                   @Path(PRODUCT_ID) String productId);

    @GET(ProductUrl.URL_ADD_PRODUCT + "/{" + PRODUCT_ID + "}")
    Observable<Response<DataResponse<ProductViewModel>>> getProductDetail(
            @Path(PRODUCT_ID) String productId,
            @Query("show_variant") int showVariant);
}
