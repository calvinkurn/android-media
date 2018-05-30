package com.tokopedia.transaction.orders.orderdetails.source;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import com.google.gson.Gson;
import com.tokopedia.abstraction.common.data.model.response.GraphqlResponse;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.var.TkpdCache;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.orders.orderdetails.data.DetailsData;
import com.tokopedia.transaction.orders.orderdetails.domain.OrderDetailsUseCase;
import com.tokopedia.usecase.RequestParams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by baghira on 19/03/18.
 */

public class CloudOrderDetailsDataSource {
    private Context context;
    private OrderDetailsDataApi orderDetailsDataApi;

    public CloudOrderDetailsDataSource(OrderDetailsDataApi orderDetailsDataApi, @ApplicationContext Context context) {
        this.orderDetailsDataApi = orderDetailsDataApi;
        this.context = context;
    }

    public Observable<DetailsData> getOrderDetails(RequestParams requestParams) {
        return orderDetailsDataApi.getOrderListData(getPayload(requestParams).getParameters()).map(new Func1<Response<GraphqlResponse<DetailsData>>, DetailsData>() {
            @Override
            public DetailsData call(Response<GraphqlResponse<DetailsData>> graphqlResponseResponse) {
                if (graphqlResponseResponse != null && graphqlResponseResponse.isSuccessful()) {
                    return graphqlResponseResponse.body().getData();
                }
                return null;
            }
        });
    }

    private RequestParams getPayload(RequestParams params) {
        Map<String, Object> variables = new HashMap<>();
        variables.put(OrderDetailsUseCase.ORDER_CATEGORY, params.getObject(OrderDetailsUseCase.ORDER_CATEGORY));
        variables.put(OrderDetailsUseCase.ORDER_ID, params.getString(OrderDetailsUseCase.ORDER_ID, "1"));
        RequestParams requestParams = RequestParams.create();
        requestParams.putObject("query", GraphqlHelper.loadRawString(context.getResources(),
                R.raw.orderdetails));
        requestParams.putObject("variables", variables);
        return requestParams;
    }
}
