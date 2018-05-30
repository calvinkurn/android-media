package com.tokopedia.transaction.orders.orderlist.source;

import android.content.Context;
import android.util.Log;

import com.tokopedia.abstraction.common.data.model.response.GraphqlResponse;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.orders.orderlist.data.Data;
import com.tokopedia.transaction.orders.orderlist.domain.OrderListUseCase;
import com.tokopedia.transaction.orders.orderlist.source.api.OrderListDataApi;
import com.tokopedia.usecase.RequestParams;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by baghira on 19/03/18.
 */

public class CloudOrderListDataSource {
    private final Context context;
    private OrderListDataApi orderListDataApi;

    public CloudOrderListDataSource(OrderListDataApi orderListDataApi,Context context){
        this.orderListDataApi = orderListDataApi;
        this.context = context;
    }

    public Observable<Data> getOrderList(RequestParams requestParams){

        return orderListDataApi.getOrderListData(getPayload(requestParams).getParameters()).map(new Func1<Response<GraphqlResponse<Data>>, Data>() {
            @Override
            public Data call(Response<GraphqlResponse<Data>> graphqlResponseResponse) {
                if(graphqlResponseResponse != null && graphqlResponseResponse.isSuccessful()){
                    return graphqlResponseResponse.body().getData();
                }
                return null;
            }
        });
    }

    private RequestParams getPayload(RequestParams params) {
        Log.e("sandeep","page="+Integer.parseInt(params.getString(OrderListUseCase.PAGE, "1")));
        Map<String, Object> variables = new HashMap<>();
        variables.put(OrderListUseCase.PER_PAGE, params.getInt(OrderListUseCase.PER_PAGE, 10));
        variables.put(OrderListUseCase.PAGE, params.getInt(OrderListUseCase.PAGE,1));
        variables.put(OrderListUseCase.ORDER_CATEGORY, params.getObject(OrderListUseCase.ORDER_CATEGORY));
        RequestParams requestParams = RequestParams.create();
        requestParams.putObject("query", GraphqlHelper.loadRawString(context.getResources(),
                R.raw.orderlist));
        requestParams.putObject("variables", variables);
        return requestParams;
    }}
