package com.tokopedia.transaction.orders.orderdetails.source;

import com.tokopedia.abstraction.common.data.model.response.GraphqlResponse;
import com.tokopedia.transaction.orders.orderdetails.data.DetailsData;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by baghira on 11/04/18.
 */

public interface OrderDetailsDataApi {
    @POST("./")
    Observable<Response<GraphqlResponse<DetailsData>>> getOrderListData(@Body Map<String, Object> requestBody);
}
