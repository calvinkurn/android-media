package com.tokopedia.transaction.orders.orderlist.source;

import com.tokopedia.abstraction.common.data.model.response.GraphqlResponse;
import com.tokopedia.transaction.orders.orderlist.data.Data;
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

public class CloudOrderListDataSource {
    private OrderListDataApi orderListDataApi;
    // private AnalyticsCacheHandler analyticsCacheHandler;

    public static String QUERY_START = "{\"query\":\"query  { orders(";
    public static String QUERY_END = ") {             categoryName       category       id       createdAt       status      statusColor       statusStr       paymentID      paymentData{      label      value      textColor      backgroundColor      imageUrl    }      invoiceRefNum       invoiceRefURL      metaData { label value textColor backgroundColor } totalInvoices       title       items {       imageUrl       name       }       itemCount       conditionalInfo {       text       color {       border       background       }       }       dotMenuList {           name       uri       }       actionButtons {       label           buttonType       uri       mappingUri       weight       color {       border       background       }           popup {       title               popuptext               actionButtons {       label                   buttonType       uri       mappingUri       weight       color {       border       background       }               }           }       }   } }\"}";

    public CloudOrderListDataSource(OrderListDataApi orderListDataApi){
        this.orderListDataApi = orderListDataApi;
    }

    public Observable<Data> getOrderList(RequestParams requestParams){
        String QUERY = "orderCategory:"+requestParams.getObject(OrderListUseCase.ORDER_CATEGORY)+" Page:"+requestParams.getInt(OrderListUseCase.FIRST, 1)+" PerPage:"+requestParams.getInt(OrderListUseCase.OFFSET, 10);
        return orderListDataApi.getOrderListData(QUERY_START+QUERY+QUERY_END).map(new Func1<Response<GraphqlResponse<Data>>, Data>() {
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
