package com.tokopedia.transaction.orders.common;

import android.content.Context;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.common.network.interceptor.TkpdAuthInterceptor;
import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.core.util.SessionHandler;

import java.io.IOException;

import javax.inject.Inject;

import okhttp3.Request;
import okhttp3.Response;

public class OrderListAuthInterceptor extends TkpdAuthInterceptor {


    Context mContext;

    @Inject
    public OrderListAuthInterceptor(Context context, AbstractionRouter abstractionRouter, UserSession userSession) {
        super(context, abstractionRouter, userSession);
        mContext = context;
    }
    private static final String HEADER_SESSION_ID = "Tkpd-SessionId";

    @Override
    public Response intercept(Chain chain) throws IOException {
            final Request originRequest = chain.request();
            Request.Builder newRequest = chain.request().newBuilder();

            generateHmacAuthRequest(originRequest, newRequest);
            newRequest.addHeader(HEADER_SESSION_ID,userSession.getDeviceId());
            newRequest.removeHeader("Authorization")
                    .addHeader("Accounts-Authorization", "Bearer " + SessionHandler.getAccessToken())
                    .addHeader("Tkpd-UserId", SessionHandler.getLoginID(mContext));

            final Request finalRequest = newRequest.build();
            Response response = getResponse(chain, finalRequest);

            if (!response.isSuccessful()) {
                throwChainProcessCauseHttpError(response);
            }

            String bodyResponse = response.body().string();
            checkResponse(bodyResponse, response);

            return createNewResponse(response, bodyResponse);
    }
}