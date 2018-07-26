package com.tokopedia.inbox.rescenter.network;


import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.common.utils.GlobalConfig;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author by yfsx on 26/07/18.
 */
public class ResolutionInterceptor implements Interceptor {

    private static final String KEY_AUTHORIZATION = "Authorization";
    private static final String KEY_XAPPVERSION = "X-App-Version";
    private static final String KEY_CONTENTTYPE = "Content-Type";
    private static final String KEY_XDEVICE = "X-Device";
    private static final String CONTENT_TYPE = "application/x-www-form-urlencoded";
    private static final String BEARER = "Bearer";
    private final UserSession userSession;

    public ResolutionInterceptor(UserSession userSession) {
        this.userSession = userSession;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder newRequest = chain.request().newBuilder();
        newRequest = addResolutionInterceptor(newRequest);

        return chain.proceed(newRequest.build());
    }

    private Request.Builder addResolutionInterceptor(Request.Builder newRequest) {
        newRequest.addHeader(KEY_AUTHORIZATION, BEARER + " "
                + userSession.getAccessToken());
        newRequest.addHeader(KEY_XDEVICE, "android-" + GlobalConfig.VERSION_NAME);
        newRequest.addHeader(KEY_XAPPVERSION, "android-" + GlobalConfig.VERSION_NAME);
        newRequest.addHeader(KEY_CONTENTTYPE, CONTENT_TYPE);
        return newRequest;
    }
}
