package com.tokopedia.di;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.network.service.AccountsService;

import javax.inject.Named;

import dagger.Component;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

import static com.tokopedia.di.UserModule.BEARER_SERVICE;
import static com.tokopedia.di.UserModule.HMAC_SERVICE;
import static com.tokopedia.di.UserModule.WS_SERVICE;

/**
 * @author by yfsx on 09/04/18.
 */

@UserScope
@Component(modules = UserModule.class, dependencies = BaseAppComponent.class)
public interface UserComponent {

    OkHttpClient provideOkHttpClient();

    Retrofit.Builder retrofitBuilder();

    @Named(BEARER_SERVICE)
    AccountsService provideBearerAccountService();

    @Named(HMAC_SERVICE)
    AccountsService provideHMACAccountService();

    @Named(WS_SERVICE)
    AccountsService provideWSAccountService();

    SessionHandler provideSessionHandler();

    GlobalCacheManager provideGlobalCacheManager();
}
