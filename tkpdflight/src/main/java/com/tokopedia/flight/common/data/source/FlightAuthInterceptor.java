package com.tokopedia.flight.common.data.source;

import android.content.Context;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.di.qualifier.AuthKeyQualifier;
import com.tokopedia.abstraction.common.di.qualifier.FreshAccessTokenQualifier;
import com.tokopedia.abstraction.common.network.interceptor.TkpdAuthInterceptor;
import com.tokopedia.abstraction.common.utils.AuthUtil;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Map;

import javax.inject.Inject;

import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by zulfikarrahman on 11/16/17.
 */

public class FlightAuthInterceptor extends TkpdAuthInterceptor {

    private static final String CONTENT_TYPE = "";

    @Inject
    public FlightAuthInterceptor(@AuthKeyQualifier String authKey, @ApplicationContext Context context, @FreshAccessTokenQualifier String freshAccessToken, AbstractionRouter abstractionRouter,
                                 UserSession userSession) {
        super(authKey, context, freshAccessToken, abstractionRouter, userSession);
        this.maxRetryAttempt = 0;
    }


    @Override
    protected Map<String, String> getHeaderMap(String path, String strParam, String method, String authKey, String contentTypeHeader) {
        String newPath = path.replace("/travel", "");
        return AuthUtil.generateHeadersWithXUserId(newPath,strParam,method,authKey,contentTypeHeader,userSession.getUserId(), userSession.getDeviceId());
    }

    protected Response getResponse(Chain chain, Request request) throws IOException {
        try {
            return chain.proceed(request);
        } catch (Error e) {
            throw new UnknownHostException("tidak ada koneksi internet");
        }
    }
}
