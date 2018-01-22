package com.tokopedia.tokocash.network.interceptor;

import android.content.Context;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.di.qualifier.AuthKeyQualifier;
import com.tokopedia.abstraction.common.di.qualifier.FreshAccessTokenQualifier;
import com.tokopedia.abstraction.common.network.interceptor.TkpdAuthInterceptor;
import com.tokopedia.abstraction.common.utils.AuthUtil;
import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.tokopedia.tokocash.network.TkpdTokoCashResponse;
import com.tokopedia.tokocash.network.TokoCashSession;
import com.tokopedia.tokocash.network.WalletTokenRefresh;
import com.tokopedia.tokocash.network.exception.HttpErrorException;
import com.tokopedia.tokocash.network.exception.WalletException;

import java.io.IOException;
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
    private TokoCashSession tokoCashSession;
    private AbstractionRouter abstractionRouter;
    @Inject
    WalletTokenRefresh walletTokenRefresh;

    @Inject
    public WalletAuthInterceptor(@AuthKeyQualifier String authKey, @ApplicationContext Context context,
                                 @FreshAccessTokenQualifier String freshAccessToken, AbstractionRouter abstractionRouter,
                                 UserSession userSession, TokoCashSession tokoCashSession) {
        super(authKey, context, freshAccessToken, abstractionRouter, userSession);
        this.tokoCashSession = tokoCashSession;
        this.abstractionRouter = abstractionRouter;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        final Request originRequest = chain.request();
        Request.Builder newRequest = chain.request().newBuilder();

        generateHmacAuthRequest(originRequest, newRequest);

        final Request finalRequest = newRequest.build();
        Response response = getResponse(chain, finalRequest);

        if (isNeedRelogin(response)) {
            doRelogin();
            response = getResponse(chain, finalRequest);
        }

        if (isUnauthorizeWalletToken(finalRequest, response)) {
            walletTokenRefresh.refreshToken();
            Request newRequestWallet = reCreateRequestWithNewAccessToken(chain);
            Response responseNew = chain.proceed(newRequestWallet);
            if (isUnauthorizeWalletToken(newRequestWallet, responseNew)) {
                abstractionRouter.showForceLogoutDialog();
                abstractionRouter.sendForceLogoutAnalytics(response.request().url().toString());
            }
            return responseNew;
        }

        if (!response.isSuccessful()) {
            throwChainProcessCauseHttpError(response);
        }

        String bodyResponse = response.body().string();
        checkResponse(bodyResponse, response);

        return createNewResponse(response, bodyResponse);
    }

    private void checkResponse(String string, Response response) {
        String bodyResponse = string;
        if (isMaintenance(bodyResponse)) {
            showMaintenancePage();
        } else if (isRequestDenied(bodyResponse)) {
            showForceLogoutDialog();
        } else if (isServerError(response.code()) && !isHasErrorMessage(bodyResponse)) {
            showServerError(response);
            sendErrorNetworkAnalytics(response.request().url().toString(), response.code());
        } else if (isForbiddenRequest(bodyResponse, response.code())
                && isTimezoneNotAutomatic()) {
            showTimezoneErrorSnackbar();
        }
    }

    @Override
    public void throwChainProcessCauseHttpError(Response response) throws IOException {
        String errorBody = response.body().string();
        response.body().close();
        if (!errorBody.isEmpty()) {
            TkpdTokoCashResponse.TokoCashErrorResponse tokoCashErrorResponse =
                    TkpdTokoCashResponse.TokoCashErrorResponse.factory(errorBody, response.code());
            if (tokoCashErrorResponse.getTypeOfError()
                    == TkpdTokoCashResponse.TokoCashErrorResponse.ERROR_TOKOCASH) {
                throw new WalletException(tokoCashErrorResponse.getTokoCashErrorMessageFormatted());
            } else {
                throw new HttpErrorException(response.code());
            }
        }
        throw new HttpErrorException(response.code());
    }

    private boolean isUnauthorizeWalletToken(Request request, Response response) {
        try {
            String responseString = response.peekBody(512).string();
            return response.code() == 401 || responseString.toLowerCase().contains("invalid_request")
                    && request.header(AUTHORIZATION).contains(BEARER);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private Request reCreateRequestWithNewAccessToken(Chain chain) {
        return chain.request().newBuilder()
                .header(AUTHORIZATION, BEARER + " " + tokoCashSession.getTokenWallet())
                .build();
    }

    @Override
    protected Map<String, String> getHeaderMap(
            String path, String strParam, String method, String authKey, String contentTypeHeader) {
        Map<String, String> header = new HashMap<>();
        header.put(AuthUtil.HEADER_AUTHORIZATION, tokoCashSession.getTokenWallet());
        header.put(AuthUtil.HEADER_DEVICE, DEVICE + GlobalConfig.VERSION_NAME);
        return header;
    }
}
