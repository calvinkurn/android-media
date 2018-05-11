package com.tokopedia.transaction.orders.orderdetails.source;

import android.util.Log;

import com.google.gson.Gson;
import com.tokopedia.abstraction.common.data.model.response.GraphqlResponse;
import com.tokopedia.transaction.orders.orderdetails.data.DetailsData;
import com.tokopedia.transaction.orders.orderdetails.data.DetailsQueryModle;
import com.tokopedia.transaction.orders.orderdetails.data.Variable;
import com.tokopedia.transaction.orders.orderdetails.domain.OrderDetailsUseCase;
import com.tokopedia.transaction.orders.orderlist.data.Data;
import com.tokopedia.transaction.orders.orderlist.data.OrderCategory;
import com.tokopedia.transaction.orders.orderlist.data.QueryModle;
import com.tokopedia.transaction.orders.orderlist.domain.OrderListUseCase;
import com.tokopedia.transaction.orders.orderlist.source.api.OrderListDataApi;
import com.tokopedia.usecase.RequestParams;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by baghira on 19/03/18.
 */

public class CloudOrderDetailsDataSource {
    private final Gson gson;
    private OrderDetailsDataApi orderDetailsDataApi;
    // private AnalyticsCacheHandler analyticsCacheHandler;

    public static String QUERY_START = "query ($orderCategory: OrderCategory, $orderId: String) {" +
            "  orderDetails(orderCategory: $orderCategory, orderId: $orderId) { status {\n" +
            "      statusText\n" +
            "      status\n" +
            "      statusLabel\n" +
            "      iconUrl\n" +
            "      textColor\n" +
            "      backgroundColor\n" +
            "      fontSize\n" +
            "      __typename\n" +
            "    }\n" +
            "    conditionalInfo {\n" +
            "      text\n" +
            "      color {\n" +
            "        border\n" +
            "        background\n" +
            "        __typename\n" +
            "      }\n" +
            "      __typename\n" +
            "    }\n" +
            "    title {\n" +
            "      label\n" +
            "      value\n" +
            "      textColor\n" +
            "      backgroundColor\n" +
            "      imageUrl\n" +
            "      __typename\n" +
            "    }\n" +
            "    invoice {\n" +
            "      invoiceRefNum\n" +
            "      invoiceUrl\n" +
            "      __typename\n" +
            "    }\n" +
            "    orderToken {\n" +
            "      label\n" +
            "      value\n" +
            "      QRCodeUrl\n" +
            "      __typename\n" +
            "    }\n" +
            "    detail {\n" +
            "      label\n" +
            "      value\n" +
            "      textColor\n" +
            "      backgroundColor\n" +
            "      imageUrl\n" +
            "      __typename\n" +
            "    }\n" +
            "    additionalInfo {\n" +
            "      label\n" +
            "      value\n" +
            "      textColor\n" +
            "      backgroundColor\n" +
            "      imageUrl\n" +
            "      __typename\n" +
            "    }\n" +
            "    pricing {\n" +
            "      label\n" +
            "      value\n" +
            "      textColor\n" +
            "      backgroundColor\n" +
            "      imageUrl\n" +
            "      __typename\n" +
            "    }\n" +
            "    paymentData {\n" +
            "      label\n" +
            "      value\n" +
            "      textColor\n" +
            "      backgroundColor\n" +
            "      imageUrl\n" +
            "      __typename\n" +
            "    }\n" +
            "    contactUs {\n" +
            "      helpText\n" +
            "      helpUrl\n" +
            "      __typename\n" +
            "    }\n" +
            "    actionButtons {\n" +
            "      label\n" +
            "      buttonType\n" +
            "      uri\n" +
            "      mappingUri\n" +
            "      weight\n" +
            "      __typename\n" +
            "    }\n" +
            "    __typename }   }";
    public CloudOrderDetailsDataSource(OrderDetailsDataApi orderDetailsDataApi, Gson gson){
        this.orderDetailsDataApi = orderDetailsDataApi;
        this.gson = gson;
    }

    public Observable<DetailsData> getOrderDetails(RequestParams requestParams){
        DetailsQueryModle queryModle = new DetailsQueryModle();
        queryModle.setVariables(new Variable((OrderCategory)requestParams.getObject(OrderDetailsUseCase.ORDER_CATEGORY), requestParams.getString(OrderDetailsUseCase.ORDER_ID, "")));
        queryModle.setQuery(QUERY_START);

        return orderDetailsDataApi.getOrderListData(queryModle).map(new Func1<Response<GraphqlResponse<DetailsData>>, DetailsData>() {
            @Override
            public DetailsData call(Response<GraphqlResponse<DetailsData>> graphqlResponseResponse) {
                if(graphqlResponseResponse != null && graphqlResponseResponse.isSuccessful()){
                    return graphqlResponseResponse.body().getData();
                }
                return null;
            }
        });
    }

    private Action1<DetailsData> setToCache(){
        return new Action1<DetailsData>() {
            @Override
            public void call(DetailsData data) {
                //Log.e("sandeep",data.toString());
                // if(data != null);
                //analyticsCacheHandler.setUserDataGraphQLCache(data);
            }
        };
    }
}
