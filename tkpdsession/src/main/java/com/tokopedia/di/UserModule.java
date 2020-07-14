package com.tokopedia.di;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;

import com.chuckerteam.chucker.api.ChuckerCollector;
import com.chuckerteam.chucker.api.ChuckerInterceptor;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.network.interceptor.AccountsAuthorizationInterceptor;
import com.tokopedia.abstraction.common.network.interceptor.ErrorResponseInterceptor;
import com.tokopedia.abstraction.common.network.interceptor.TkpdAuthInterceptor;
import com.tokopedia.config.GlobalConfig;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.network.UserErrorInterceptor;
import com.tokopedia.network.UserErrorResponse;
import com.tokopedia.network.service.AccountsService;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * @author by yfsx on 09/04/18.
 */

@Module
public class UserModule {

    public static final String HMAC_SERVICE = "HMAC_SERVICE";
    public static final String BEARER_SERVICE = "BEARER_SERVICE";
    public static final String WS_SERVICE = "WS_SERVICE";
    public static final String LOGIN_CACHE = "LOGIN_CACHE";

    @UserScope
    @Provides
    GlobalCacheManager provideGlobalCacheManager() {
        return new GlobalCacheManager();
    }

    /**
     * @return https://accounts.tokopedia.com
     * with Authorization : Tkpd
     */
    @UserScope
    @Named(HMAC_SERVICE)
    @Provides
    AccountsService provideHMACAccountsService() {
        Bundle bundle = new Bundle();
        bundle.putBoolean(AccountsService.USING_HMAC, true);
        bundle.putString(AccountsService.AUTH_KEY, AuthUtil.KEY.KEY_WSV4);
        return new AccountsService(bundle);
    }

    /**
     * @param context
     * @param sessionHandler
     * @return https://accounts.tokopedia.com
     * with Authorization : Bearer {Access Token}
     */
    @UserScope
    @Named(BEARER_SERVICE)
    @Provides
    AccountsService provideBearerAccountsService(@ApplicationContext Context context,
                                                 SessionHandler sessionHandler) {
        UserSessionInterface userSession = new UserSession(context);
        Bundle bundle = new Bundle();
        String authKey = "";
        if (!TextUtils.isEmpty(userSession.getAccessToken())) {
            authKey = sessionHandler.getTokenType(context) + " " + userSession.getAccessToken();
        }
        bundle.putString(AccountsService.AUTH_KEY, authKey);
        return new AccountsService(bundle);
    }


    @UserScope
    @Provides
    AccountsService provideAccountsService() {
        Bundle bundle = new Bundle();
        return new AccountsService(bundle);
    }


    /**
     * @param context
     * @param sessionHandler
     * @return https://ws.tokopedia.com
     * with Authorization : Bearer {Access Token}
     */
    @UserScope
    @Named(WS_SERVICE)
    @Provides
    AccountsService provideWsAccountsService(@ApplicationContext Context context, SessionHandler sessionHandler) {
        UserSessionInterface userSession = new UserSession(context);
        Bundle bundle = new Bundle();
        String authKey;
        authKey = sessionHandler.getTokenType(context) + " " + userSession.getAccessToken();
        bundle.putString(AccountsService.AUTH_KEY, authKey);
        bundle.putString(AccountsService.WEB_SERVICE, AccountsService.WS);
        return new AccountsService(bundle);
    }

    @UserScope
    @Provides
    public UserErrorInterceptor provideUserErrorInterceptor() {
        return new UserErrorInterceptor(UserErrorResponse.class);
    }

    @UserScope
    @Provides
    public ChuckerInterceptor provideChuckerInterceptor(@ApplicationContext Context context) {
        ChuckerCollector collector = new ChuckerCollector(
                context, GlobalConfig.isAllowDebuggingTools());

        return new ChuckerInterceptor(
                context, collector);
    }


    @UserScope
    @Provides
    public AccountsAuthorizationInterceptor provideAccountsAuthorizationInterceptor(@ApplicationContext Context context) {
        return new AccountsAuthorizationInterceptor(context);
    }

    @UserScope
    @Provides
    public OkHttpClient provideOkHttpClient(ChuckerInterceptor chuckInterceptor,
                                            HttpLoggingInterceptor httpLoggingInterceptor,
                                            TkpdAuthInterceptor tkpdAuthInterceptor) {

        OkHttpClient.Builder bulder = new OkHttpClient.Builder()
                .addInterceptor(new ErrorResponseInterceptor(UserErrorResponse.class))
                .addInterceptor(tkpdAuthInterceptor);
        if(GlobalConfig.isAllowDebuggingTools()) {
             bulder.addInterceptor(chuckInterceptor);
             bulder.addInterceptor(httpLoggingInterceptor);
         }
        return bulder.build();
    }

    @UserScope
    @Provides
    SessionHandler providesSessionHandler(@ApplicationContext Context context) {
        return new SessionHandler(context);
    }
}
