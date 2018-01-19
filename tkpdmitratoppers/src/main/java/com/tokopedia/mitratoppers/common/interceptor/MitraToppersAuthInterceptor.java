package com.tokopedia.mitratoppers.common.interceptor;

import android.content.Context;
import android.support.v4.util.ArrayMap;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.network.interceptor.TkpdAuthInterceptor;
import com.tokopedia.abstraction.common.utils.AuthUtil;
import com.tokopedia.mitratoppers.common.constant.MitraToppersClientKey;

import java.util.Map;

import javax.inject.Inject;

public class MitraToppersAuthInterceptor extends TkpdAuthInterceptor {
    private static final String KEY_SIGNATURE = "Signature";
    public static final String KEY_UNIX_TIME = "Unix-Time";

    @Inject
    public MitraToppersAuthInterceptor(@ApplicationContext Context context,
                                       AbstractionRouter abstractionRouter,
                                       UserSession userSession) {
        super(context, abstractionRouter, userSession);
    }

    // Signature - HMAC(SecretKey, SecretKey2, URL.Path, SecretKey3, unixTimestamp, Method)
    // Unix-Time - the unix time should be < 5 minutes from current time
    // example https://phab.tokopedia.com/w/api/fintech/microfinance/preapprove-balance/
    protected Map<String, String> getHeaderMap(String path, String strParam,
                                               String method, String authKey,
                                               String contentTypeHeader) {

        Map<String, String> headerMap = new ArrayMap<>();
        String secretKey = MitraToppersClientKey.CLIENT_KEY_1;
        String secretKey2 = MitraToppersClientKey.CLIENT_KEY_2;
        String secretKey3 = MitraToppersClientKey.CLIENT_KEY_3;
        String unixTimeStamp = String.valueOf(System.currentTimeMillis() / 1000L);
        String pathTrim = path.startsWith("/") ? path.replaceFirst("/", "") : path;

        String hmacString = secretKey2 + pathTrim + secretKey3 + unixTimeStamp + method;
        String hmacSignature = AuthUtil.calculateRFC2104HMAC(hmacString, secretKey).trim();

        headerMap.put(KEY_SIGNATURE, hmacSignature);
        headerMap.put(KEY_UNIX_TIME, unixTimeStamp);
        return headerMap;
    }


}
