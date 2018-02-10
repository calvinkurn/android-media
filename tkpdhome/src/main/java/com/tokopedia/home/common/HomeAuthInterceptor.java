package com.tokopedia.home.common;

import android.content.Context;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.di.qualifier.FreshAccessTokenQualifier;
import com.tokopedia.abstraction.common.network.interceptor.TkpdAuthInterceptor;
import com.tokopedia.abstraction.common.utils.network.AuthUtil;

import java.io.IOException;
import java.net.UnknownHostException;

import javax.inject.Inject;

import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by errysuprayogi on 2/6/18.
 */

public class HomeAuthInterceptor extends TkpdAuthInterceptor {

    @Inject
    public HomeAuthInterceptor(@ApplicationContext Context context,
                               @FreshAccessTokenQualifier String freshAccessToken,
                               AbstractionRouter abstractionRouter,
                               UserSession userSession) {
        super(context, abstractionRouter, userSession, AuthUtil.KEY.KEY_WSV4);
    }

    protected Response getResponse(Chain chain, Request request) throws IOException {
        try {
            return chain.proceed(request);
        } catch (Error e) {
            throw new UnknownHostException("tidak ada koneksi internet");
        }
    }
}
