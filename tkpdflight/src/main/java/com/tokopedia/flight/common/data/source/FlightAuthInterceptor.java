package com.tokopedia.flight.common.data.source;

import android.content.Context;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.network.interceptor.TkpdAuthInterceptor;
import com.tokopedia.abstraction.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.di.qualifier.AuthKeyQualifier;
import com.tokopedia.abstraction.di.qualifier.FreshAccessTokenQualifier;
import java.util.Map;

import javax.inject.Inject;

/**
 * Created by zulfikarrahman on 11/16/17.
 */

public class FlightAuthInterceptor extends TkpdAuthInterceptor {

    private static final String CONTENT_TYPE = "";

    @Inject
    public FlightAuthInterceptor(@AuthKeyQualifier String authKey, @ApplicationContext Context context, @FreshAccessTokenQualifier String freshAccessToken, AbstractionRouter abstractionRouter) {
        super(authKey, context, freshAccessToken, abstractionRouter);
    }


    @Override
    protected Map<String, String> getHeaderMap(String path, String strParam, String method, String authKey, String contentTypeHeader) {
        return super.getHeaderMap(path, strParam, method, authKey, contentTypeHeader);
    }
}
