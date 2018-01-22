package com.tokopedia.tokocash.network.interceptor;

import android.content.Context;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.di.qualifier.AuthKeyQualifier;
import com.tokopedia.abstraction.common.di.qualifier.FreshAccessTokenQualifier;
import com.tokopedia.abstraction.common.network.interceptor.TkpdAuthInterceptor;
import com.tokopedia.abstraction.common.utils.GlobalConfig;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

/**
 * Created by nabillasabbaha on 1/17/18.
 */

public class TokoCashAuthInterceptor extends TkpdAuthInterceptor {

    @Inject
    public TokoCashAuthInterceptor(@AuthKeyQualifier String authKey, @ApplicationContext Context context,
                                   @FreshAccessTokenQualifier String freshAccessToken,
                                   AbstractionRouter abstractionRouter, UserSession userSession) {
        super(authKey, context, freshAccessToken, abstractionRouter, userSession);
    }

    @Override
    protected Map<String, String> getHeaderMap(String path, String strParam, String method, String authKey, String contentTypeHeader) {
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("Authorization", userSession.getAccessToken());
        headerMap.put("X-Device", "android-" + GlobalConfig.VERSION_NAME);
        headerMap.put("Content-Type", "application/x-www-form-urlencoded");
        return headerMap;
    }
}