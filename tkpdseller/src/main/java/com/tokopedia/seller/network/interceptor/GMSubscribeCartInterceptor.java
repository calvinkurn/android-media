package com.tokopedia.seller.network.interceptor;

import android.content.Intent;
import android.util.Log;

import com.tkpd.library.utils.AnalyticsLog;
import com.tokopedia.core.MaintenancePage;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.network.retrofit.interceptors.TkpdAuthInterceptor;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.core.util.SessionHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;

/**
 * Created by sebastianuskh on 2/2/17.
 */
public class GMSubscribeCartInterceptor extends TkpdAuthInterceptor{
    private static final String DATE_FORMAT = "EEE, dd MMM yyyy HH:mm:ss ZZZ";
    private static final String CONTENT_TYPE_JSON_UT = "application/json; charset=UTF-8";
    private static final String HEADER_AUTHORIZATION = "Authorization";
    private static final String X_TKPD_HEADER_AUTHORIZATION = "X-TKPD-Authorization";
    private static final String BEARER = "Bearer ";

    @Override
    protected Map<String, String> getHeaderMap(String path, String strParam, String method, String authKey) {
        Map<String, String> headerMap = AuthUtil.getDefaultHeaderMap(path, strParam, method, CONTENT_TYPE_JSON_UT, authKey, DATE_FORMAT);

        String xTkpdAuthorization = headerMap.get(HEADER_AUTHORIZATION);
        headerMap.put(X_TKPD_HEADER_AUTHORIZATION, xTkpdAuthorization);

        headerMap.remove(HEADER_AUTHORIZATION);
        SessionHandler sessionHandler = new SessionHandler(MainApplication.getAppContext());
        String bearerAutorization = BEARER + sessionHandler.getAccessToken(MainApplication.getAppContext());
        headerMap.put(HEADER_AUTHORIZATION, bearerAutorization);

        return headerMap;
    }
}
