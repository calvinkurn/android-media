package com.tokopedia.transaction.orders.orderlist.source;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.tokopedia.abstraction.common.data.model.response.GraphqlResponse;
import com.tokopedia.transaction.orders.orderlist.data.Data;
import com.tokopedia.transaction.orders.orderlist.data.QueryModle;
import com.tokopedia.transaction.orders.orderlist.domain.OrderListUseCase;
import com.tokopedia.transaction.orders.orderlist.source.api.OrderListDataApi;
import com.tokopedia.usecase.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by baghira on 19/03/18.
 */

public class CloudOrderListDataSource {
    private final Gson gson;
    private OrderListDataApi orderListDataApi;
    // private AnalyticsCacheHandler analyticsCacheHandler;

    public static String QUERY_START = "query  { orders(";
    public static String QUERY_END = ") {             categoryName       category       id       createdAt       status      statusColor       statusStr       paymentID      paymentData{      label      value      textColor      backgroundColor      imageUrl    }      invoiceRefNum       invoiceRefURL       totalInvoices       title       items {       imageUrl       name       }       itemCount       conditionalInfo {       text       color {       border       background       }       }       dotMenuList {           name       uri       }  paymentData { label value textColor backgroundColor imageUrl }  metaData { label value textColor backgroundColor }   actionButtons {       label           buttonType       uri       mappingUri       weight       color {       border       background       }           popup {       title               popuptext               actionButtons {       label                   buttonType       uri       mappingUri       weight       color {       border       background       }               }           }       }   } }";
    public CloudOrderListDataSource(OrderListDataApi orderListDataApi,Gson gson){
        this.orderListDataApi = orderListDataApi;
        this.gson = gson;
    }

    public Observable<Data> getOrderList(RequestParams requestParams){
        String QUERY = "orderCategory:"+requestParams.getObject(OrderListUseCase.ORDER_CATEGORY)+" Page:"+requestParams.getInt(OrderListUseCase.FIRST, 1)+" PerPage:"+requestParams.getInt(OrderListUseCase.OFFSET, 10);
        QueryModle queryModle = new QueryModle();
        queryModle.setQuery(QUERY_START+QUERY+QUERY_END);

        return orderListDataApi.getOrderListData(queryModle).map(new Func1<Response<GraphqlResponse<Data>>, Data>() {
            @Override
            public Data call(Response<GraphqlResponse<Data>> graphqlResponseResponse) {
                if(graphqlResponseResponse != null && graphqlResponseResponse.isSuccessful()){
                    return graphqlResponseResponse.body().getData();
                }
                return null;
            }
        });
    }

    private Action1<Data> setToCache(){
        return new Action1<Data>() {
            @Override
            public void call(Data data) {
                //Log.e("sandeep",data.toString());
                // if(data != null);
                //analyticsCacheHandler.setUserDataGraphQLCache(data);
            }
        };
    }
}
