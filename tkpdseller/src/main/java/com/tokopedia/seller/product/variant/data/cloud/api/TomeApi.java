package com.tokopedia.seller.product.variant.data.cloud.api;

import com.tokopedia.seller.common.data.response.DataResponse;
import com.tokopedia.seller.product.variant.data.model.variantbycat.ProductVariantByCatModel;

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
    String PRD_ID = "p_id";
    String GET_VARIANT_BY_CAT_PATH = "v1/web-service/category/get_variant";
    String GET_VARIANT_BY_PRD_PATH = "v2/web-service/product/get_variant";

    @GET(GET_VARIANT_BY_CAT_PATH)
    Observable<Response<DataResponse<List<ProductVariantByCatModel>>>> getProductVariantByCat(@Query(CAT_ID) long categoryId);

    @GET(GET_VARIANT_BY_PRD_PATH)
    Observable<Response<DataResponse<String>>> getProductVariantByPrd(@Query(PRD_ID) long productId);
}
