package com.tokopedia.seller.transaction.neworder.data;

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
    @POST("https://ws.tokopedia.com/v4/myshop-order/" + "get_order_new.pl")
    Observable<Response<NewOrderResult>> getOrderNew(@FieldMap TKPDMapParam<String, String> var1);
}
