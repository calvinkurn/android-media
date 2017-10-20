package com.tokopedia.topads.common.util;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.network.retrofit.interceptors.TkpdAuthInterceptor;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.core.util.SessionHandler;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

/**
 * Created by normansyahputa on 10/20/17.
 */

public class TopAdsAuthInterceptor extends TkpdAuthInterceptor {
    private static final String HEADER_DATE_FORMAT = "dd MMM yy HH:mm ZZZ";
    private static final String CONTENT_TYPE = "";
    private static final String HEADER_DATE = "X-Date";
    private static final String HEADER_DEVICE = "X-Device";
    private static final String HEADER_USER_ID = "Tkpd-UserId";
    private static final String HEADER_AUTHORIZATION = "Authorization";
    private static final String HEADER_X_AUTHORIZATION = "X-Tkpd-Authorization";
    private String bearerToken;

    public TopAdsAuthInterceptor(String bearerToken) {
        this.bearerToken = bearerToken;
    }

    protected Map<String, String> getHeaderMap(String path, String strParam, String method, String authKey, String contentTypeHeader) {
        Map headerMap = AuthUtil.getDefaultHeaderMap(path, strParam, method, "", authKey, "dd MMM yy HH:mm ZZZ");
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yy HH:mm ZZZ", Locale.ENGLISH);
        String date = dateFormat.format(new Date());
        headerMap.put("X-Date", date);
        headerMap.put("Tkpd-UserId", SessionHandler.getLoginID(MainApplication.getAppContext()));
        headerMap.put("X-Device", "android-" + GlobalConfig.VERSION_NAME);
        headerMap.put("X-Tkpd-Authorization", headerMap.get("Authorization"));
        headerMap.put("Authorization", this.bearerToken);
        return headerMap;
    }
}
