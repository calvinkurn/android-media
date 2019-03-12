package com.tokopedia.di;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;

import com.readystatesoftware.chuck.ChuckInterceptor;
import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.network.interceptor.AccountsAuthorizationInterceptor;
import com.tokopedia.abstraction.common.network.interceptor.ErrorResponseInterceptor;
import com.tokopedia.abstraction.common.network.interceptor.TkpdAuthInterceptor;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.network.UserErrorInterceptor;
import com.tokopedia.network.UserErrorResponse;
import com.tokopedia.network.service.AccountsService;

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
        Bundle bundle = new Bundle();
        String authKey = "";
        if (!TextUtils.isEmpty(sessionHandler.getAccessToken(context))) {
            authKey = sessionHandler.getTokenType(context) + " " + sessionHandler
                    .getAccessToken(context);
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
        Bundle bundle = new Bundle();
        String authKey;
        authKey = sessionHandler.getTokenType(context) + " " + sessionHandler.getAccessToken(context);
        bundle.putString(AccountsService.AUTH_KEY, authKey);
        bundle.putString(AccountsService.WEB_SERVICE, AccountsService.WS);
        return new AccountsService(bundle);
    }

    @UserScope
    @Provides
    public AnalyticTracker provideAnalyticTracker(@ApplicationContext Context context) {
        if (context instanceof AbstractionRouter) {
            return ((AbstractionRouter) context).getAnalyticTracker();
        }
        throw new RuntimeException("App should implement " + AbstractionRouter.class.getSimpleName());
    }
    @UserScope
    @Provides
    public UserErrorInterceptor provideUserErrorInterceptor() {
        return new UserErrorInterceptor(UserErrorResponse.class);
    }

    @UserScope
    @Provides
    public ChuckInterceptor provideChuckInterceptor(@ApplicationContext Context context) {
        return new ChuckInterceptor(context).showNotification(GlobalConfig.isAllowDebuggingTools());
    }


    @UserScope
    @Provides
    public AccountsAuthorizationInterceptor provideAccountsAuthorizationInterceptor(@ApplicationContext Context context) {
        return new AccountsAuthorizationInterceptor(context);
    }

    @UserScope
    @Provides
    public OkHttpClient provideOkHttpClient(ChuckInterceptor chuckInterceptor,
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
