package com.tokopedia.tokocash.network.interceptor;

import android.content.Context;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.network.interceptor.TkpdAuthInterceptor;
import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.tokopedia.abstraction.common.utils.network.AuthUtil;
import com.tokopedia.tokocash.network.WalletUserSession;
import com.tokopedia.tokocash.network.api.WalletUrl;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by nabillasabbaha on 10/12/17.
 */

public class WalletAuthInterceptor extends TkpdAuthInterceptor {

    public final static String BEARER = "Bearer";
    private final static String AUTHORIZATION = "authorization";
    private final static String DEVICE = "android-";
    private static final String HEADER_DEVICE = "X-Device";

    private WalletUserSession walletUserSession;

    @Inject
    public WalletAuthInterceptor(@ApplicationContext Context context, AbstractionRouter abstractionRouter,
                                 UserSession userSession, WalletUserSession walletUserSession) {
        super(context, abstractionRouter, userSession);
        this.walletUserSession = walletUserSession;
        maxRetryAttempt = 0;
    }

    @Override
    protected Map<String, String> getHeaderMap(
            String path, String strParam, String method, String authKey, String contentTypeHeader) {
        if (isUserInactiveTokoCash()) {
            Map<String, String> headerMap = AuthUtil.generateHeadersWithXUserIdXMsisdn(path, method,
                    WalletUrl.KeyHmac.HMAC_PENDING_CASHBACK, contentTypeHeader,
                    walletUserSession.getPhoneNumber(), userSession.getUserId());
            return headerMap;
        } else {
            Map<String, String> header = new HashMap<>();
            header.put(AUTHORIZATION, BEARER + " " + walletUserSession.getTokenWallet());
            header.put(HEADER_DEVICE, DEVICE + GlobalConfig.VERSION_NAME);
            return header;
        }
    }

    @Override
    protected Response getResponse(Chain chain, Request request) throws IOException {
        try {
            return chain.proceed(request);
        } catch (Error e) {
            throw new UnknownHostException("tidak ada koneksi internet");
        }
    }

    private boolean isUserInactiveTokoCash() {
        return walletUserSession.getTokenWallet().equals("");
    }
}
