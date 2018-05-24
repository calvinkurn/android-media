package com.tokopedia.transaction.orders.orderlist.source.api;

import com.tokopedia.abstraction.common.data.model.response.GraphqlResponse;
import com.tokopedia.transaction.orders.orderlist.data.Data;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by baghira on 11/04/18.
 */

public interface OrderListDataApi {
    @POST("./")
    Observable<Response<GraphqlResponse<Data>>> getOrderListData(@Body Map<String, Object> requestBody);
}
