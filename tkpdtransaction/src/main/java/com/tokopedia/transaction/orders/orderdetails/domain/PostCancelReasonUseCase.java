package com.tokopedia.transaction.orders.orderdetails.domain;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.tokopedia.common.network.data.model.RequestType;
import com.tokopedia.common.network.data.model.RestRequest;
import com.tokopedia.common.network.domain.RestRequestSupportInterceptorUseCase;
import com.tokopedia.transaction.opportunity.data.pojo.CancelReplacementPojo;
import com.tokopedia.transaction.orders.orderdetails.data.DataResponseCommon;
import com.tokopedia.usecase.RequestParams;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import okhttp3.Interceptor;

public class PostCancelReasonUseCase extends RestRequestSupportInterceptorUseCase {

    private RequestParams params;
    private String url = "";

    @Inject
    public PostCancelReasonUseCase(List<Interceptor> interceptor, Context context) {
        super(interceptor, context);
    }

    public void setRequestParams(RequestParams params) {
        this.params = params;
    }

    public void cancelOrReplaceOrder(String url) {
        this.url = url;
    }

    @Override
    protected List<RestRequest> buildRequest(RequestParams requestParams) {
        List<RestRequest> tempRequest = new ArrayList<>();
        Map<String, Object> params = this.params.getParameters();

        Type token = new TypeToken<DataResponseCommon<CancelReplacementPojo>>() {
        }.getType();

        RestRequest restRequest1 = new RestRequest.Builder(this.url, token)
                .setRequestType(RequestType.POST)
                .setBody(params)
                .build();
        tempRequest.add(restRequest1);
        return tempRequest;
    }
}
