package com.tokopedia.shop.common.data.source.cloud.api;

import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.interfaces.merchant.shop.info.ShopInfo;
import com.tokopedia.shop.common.constant.ShopUrl;
import com.tokopedia.shop.note.data.source.cloud.model.ResponseList;
import com.tokopedia.shop.note.data.source.cloud.model.ShopNote;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by Hendry on 4/20/2017.
 */

/**
 * Should migrate to tome
 */
@Deprecated
public interface WS4ShopApi {

    @FormUrlEncoded
    @POST(ShopUrl.SHOP_INFO_PATH)
    Observable<Response<DataResponse<ShopInfo>>> getShopInfo(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(ShopUrl.SHOP_NOTE_PATH)
    Observable<Response<DataResponse<ResponseList<ShopNote>>>> getShopNotes(@FieldMap Map<String, String> params);
}
