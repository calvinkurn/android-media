package com.tokopedia.di;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;

import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.network.apiservices.accounts.AccountsService;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.util.SessionHandler;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;


/**
 * @author by nisie on 10/10/17.
 */

@Module
public class
SessionModule {

    private static final String HMAC_SERVICE = "HMAC_SERVICE";
    private static final String BEARER_SERVICE = "BEARER_SERVICE";
    private static final String WS_SERVICE = "WS_SERVICE";

    @SessionScope
    @Provides
    GlobalCacheManager provideGlobalCacheManager() {
        return new GlobalCacheManager();
    }

    /**
     * @return https://accounts.tokopedia.com
     * with Authorization : Tkpd
     */
    @SessionScope
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
    @SessionScope
    @Named(BEARER_SERVICE)
    @Provides
    AccountsService provideBearerAccountsService(@ApplicationContext Context context,
                                                 SessionHandler sessionHandler) {
        Bundle bundle = new Bundle();
        String authKey = "";
        if (!TextUtils.isEmpty(sessionHandler.getAccessToken(context)))
            authKey = sessionHandler.getTokenType(context) + " " + sessionHandler
                    .getAccessToken(context);
        bundle.putString(AccountsService.AUTH_KEY, authKey);
        return new AccountsService(bundle);
    }


    /**
     * @param context
     * @param sessionHandler
     * @return https://ws.tokopedia.com
     * with Authorization : Bearer {Access Token}
     */
    @SessionScope
    @Named(WS_SERVICE)
    @Provides
    AccountsService provideWsAccountsService(@ApplicationContext Context context,
                                             SessionHandler sessionHandler) {
        Bundle bundle = new Bundle();
        String authKey;
        authKey = sessionHandler.getTokenType(context) + " " + sessionHandler.getAccessToken(context);
        bundle.putString(AccountsService.AUTH_KEY, authKey);
        bundle.putString(AccountsService.WEB_SERVICE, AccountsService.WS);
        return new AccountsService(bundle);
    }

}
