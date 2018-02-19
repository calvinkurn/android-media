package com.tokopedia.shop.common.data.source.cloud.api;

import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.interfaces.merchant.shop.info.ShopInfo;
import com.tokopedia.shop.common.constant.ShopParamApiContant;
import com.tokopedia.shop.common.constant.ShopUrl;
import com.tokopedia.shop.note.data.source.cloud.model.ShopNoteDetail;
import com.tokopedia.shop.note.data.source.cloud.model.ShopNoteList;
import com.tokopedia.shop.product.data.source.cloud.model.ShopProductList;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by Hendry on 4/20/2017.
 */

public interface ShopApi {

    @GET(ShopUrl.SHOP_INFO_PATH)
    Observable<Response<DataResponse<ShopInfo>>> getShopInfo(@Query(ShopParamApiContant.SHOP_ID) String shopId);

    @GET(ShopUrl.SHOP_INFO_PATH)
    Observable<Response<DataResponse<ShopInfo>>> getShopInfoByDomain(@Query(ShopParamApiContant.SHOP_ID) String shopId);

    @GET(ShopUrl.SHOP_NOTE_PATH)
    Observable<Response<DataResponse<ShopNoteList>>> getShopNotes(@Query(ShopParamApiContant.SHOP_ID) String shopId);

    @GET(ShopUrl.SHOP_NOTE_PATH)
    Observable<Response<DataResponse<ShopNoteDetail>>> getShopNoteDetail(@Query(ShopParamApiContant.SHOP_NODE_ID) String shopNoteId);

    @GET(ShopUrl.SHOP_PRODUCT_PATH)
    Observable<Response<DataResponse<ShopProductList>>> getShopProductList(@QueryMap Map<String, String> params);

    @GET
    Observable<Response<DataResponse<ShopProductList>>> getShopProductList(@Url String url, @QueryMap Map<String, String> params);
}
