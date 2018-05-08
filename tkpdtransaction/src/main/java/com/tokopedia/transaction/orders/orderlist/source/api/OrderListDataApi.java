package com.tokopedia.transaction.orders.orderlist.source.api;

import com.tokopedia.abstraction.common.data.model.response.GraphqlResponse;
import com.tokopedia.transaction.orders.common.url.OrderURL;
import com.tokopedia.transaction.orders.orderlist.data.Data;

import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by baghira on 11/04/18.
 */

public interface OrderListDataApi {
    @POST("./")
    @Headers({"Content-Type: application/json"})
    Observable<Response<GraphqlResponse<Data>>> getOrderListData(@Body String requestBody);
}
