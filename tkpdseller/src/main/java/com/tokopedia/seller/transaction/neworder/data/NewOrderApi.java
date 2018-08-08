package com.tokopedia.seller.transaction.neworder.data;

import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.seller.transaction.neworder.data.source.cloud.model.neworder.NewOrderResult;

import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by zulfikarrahman on 7/10/17.
 */

public interface NewOrderApi {
    @FormUrlEncoded
    @POST(TkpdBaseURL.Shop.URL_ACTION_SHOP_ORDER + TkpdBaseURL.Shop.PATH_GET_ORDER_NEW)
    Observable<Response<NewOrderResult>> getOrderNew(@FieldMap TKPDMapParam<String, String> var1);
}
