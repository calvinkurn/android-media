package com.tokopedia.seller.shop.setting.data.datasource.cloud;

import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.seller.common.data.response.DataResponse;
import com.tokopedia.seller.shop.setting.data.model.DataResponseShopSchedule;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by zulfikarrahman on 9/15/17.
 */

public interface ShopScheduleApi {

    @FormUrlEncoded
    @POST(TkpdBaseURL.Shop.PATH_MY_SHOP_INFO + TkpdBaseURL.Shop.PATH_UPDATE_SHOP_CLOSE)
    Observable<Response<DataResponse<DataResponseShopSchedule>>> updateShopClose(@FieldMap Map<String, String> params);
}
