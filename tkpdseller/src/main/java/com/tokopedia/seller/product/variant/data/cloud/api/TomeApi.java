package com.tokopedia.seller.product.variant.data.cloud.api;

import com.tokopedia.seller.common.data.response.DataResponse;
import com.tokopedia.seller.product.variant.data.model.ProductVariantModel;

import java.util.List;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by hendry on 24/05/17.
 */

public interface TomeApi {
    String CAT_ID = "cat_id";
    String GET_VARIANT_PATH = "v1/web-service/category/get_variant";

    @GET(GET_VARIANT_PATH)
    Observable<Response<DataResponse<List<ProductVariantModel>>>> getProductVariant(@Query(CAT_ID) long categoryId);
}
