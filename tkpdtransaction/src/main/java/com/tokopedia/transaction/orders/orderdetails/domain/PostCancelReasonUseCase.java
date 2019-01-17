package com.tokopedia.transaction.orders.orderdetails.domain;

import android.content.Context;

import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.common.network.data.model.RequestType;
import com.tokopedia.common.network.data.model.RestRequest;
import com.tokopedia.common.network.domain.RestRequestSupportInterceptorUseCase;
import com.tokopedia.common.network.domain.RestRequestUseCase;
import com.tokopedia.usecase.RequestParams;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import okhttp3.Interceptor;

public class PostCancelReasonUseCase extends RestRequestSupportInterceptorUseCase {

    private RequestParams params;
    private int cancelOrReplacement = 1;
    public static final String REQUEST_BODY="request_body";

    @Inject
    public PostCancelReasonUseCase(Interceptor interceptor, Context context) {
        super(interceptor,context);
    }

    public void setRequestParams(RequestParams params) {
        this.params = params;
    }

    public void cancelOrReplaceOrder(int cancelOrReplacement) {
        this.cancelOrReplacement = cancelOrReplacement;
    }
    @Override
    protected List<RestRequest> buildRequest(RequestParams requestParams) {
        List<RestRequest> tempRequest = new ArrayList<>();
        Map<String, Object> params = this.params.getParameters();
        Map<String, Object> headerParams = new HashMap<>();
        headerParams.put("device_time", String.valueOf((new Date().getTime()) / 1000));

        String url = "";
        if (this.cancelOrReplacement == 1) {
            url = "https://ws-staging.tokopedia.com/v4/action/tx-order/request_cancel_order.pl";
        } else {
            url = "https://ws-staging.tokopedia.com/v4/replacement/cancel";
        }
        Type token = new TypeToken<DataResponse<JsonObject>>() {
        }.getType();

        RestRequest restRequest1 = new RestRequest.Builder(url, token)
                .setRequestType(RequestType.POST)
                .setBody("")
                .setQueryParams(params)
                .setHeaders(headerParams)
                .build();
        tempRequest.add(restRequest1);
        return tempRequest;
    }
}
