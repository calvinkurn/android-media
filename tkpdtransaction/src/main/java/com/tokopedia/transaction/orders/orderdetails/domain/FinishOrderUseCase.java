package com.tokopedia.transaction.orders.orderdetails.domain;

import android.content.Context;

import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.abstraction.common.network.interceptor.ErrorResponseInterceptor;
import com.tokopedia.common.network.data.model.RequestType;
import com.tokopedia.common.network.data.model.RestRequest;
import com.tokopedia.common.network.domain.RestRequestSupportInterceptorUseCase;
import com.tokopedia.usecase.RequestParams;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import okhttp3.Interceptor;

public class FinishOrderUseCase extends RestRequestSupportInterceptorUseCase {

    private RequestParams params;
    private String endpoint;

    @Inject
    public FinishOrderUseCase(List<Interceptor> interceptor, Context context) {
         super(interceptor,context);
    }

    public void setRequestParams(RequestParams params) {
        this.params = params;
    }

    @Override
    protected List<RestRequest> buildRequest(RequestParams requestParams) {
        List<RestRequest> tempRequest = new ArrayList<>();
        Map<String, Object> params = this.params.getParameters();

        Type token = new TypeToken<DataResponse<JsonObject>>() {
        }.getType();

        RestRequest restRequest1 = new RestRequest.Builder(this.endpoint, token)
                .setRequestType(RequestType.POST)
                .setBody("")
                .setQueryParams(params)
                .build();
        tempRequest.add(restRequest1);
        return tempRequest;
    }

    public void setEndPoint(String url) {
        this.endpoint = url;
    }
}
