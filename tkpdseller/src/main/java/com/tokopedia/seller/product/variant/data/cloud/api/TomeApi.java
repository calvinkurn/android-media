package com.tokopedia.seller.product.variant.data.cloud.api;

import com.tokopedia.core.shopinfo.models.shopmodel.ShopModel;
import com.tokopedia.seller.common.data.response.DataResponse;
import com.tokopedia.seller.product.variant.data.model.variantbycat.ProductVariantByCatModel;
import com.tokopedia.seller.product.variant.data.model.variantbyprd.ProductVariantByPrdModel;

import java.util.List;
import java.util.Map;

import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

import static com.tokopedia.core.network.constants.TkpdBaseURL.Shop.PATH_SHOP_TOME;

/**
 * Created by hendry on 24/05/17.
 */

public interface TomeApi {
    String CAT_ID = "cat_id";
    String PRD_ID = "p_id";
    String SHOP_ID = "shop_id";
    String GET_VARIANT_BY_CAT_PATH = "v1/web-service/category/get_variant";
    String GET_VARIANT_BY_PRD_PATH = "v2/web-service/product/get_variant";

    @GET(GET_VARIANT_BY_CAT_PATH)
    Observable<Response<DataResponse<List<ProductVariantByCatModel>>>> getProductVariantByCat(@Query(CAT_ID) long categoryId);

    @GET(GET_VARIANT_BY_PRD_PATH)
    Observable<Response<DataResponse<ProductVariantByPrdModel>>> getProductVariantByPrd(@Query(PRD_ID) long productId);

    @GET(PATH_SHOP_TOME)
    Observable<Response<DataResponse<ShopModel>>> getShopInfo(@Query(SHOP_ID) String shopId);
}
