package com.tokopedia.tkpdstream.common.di.module;

import android.content.Context;

import com.readystatesoftware.chuck.ChuckInterceptor;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.network.interceptor.AccountsAuthorizationInterceptor;
import com.tokopedia.abstraction.common.network.interceptor.ErrorResponseInterceptor;
import com.tokopedia.abstraction.common.network.interceptor.TkpdAuthInterceptor;
import com.tokopedia.tkpdstream.common.di.scope.StreamScope;
import com.tokopedia.tkpdstream.common.network.StreamErrorInterceptor;
import com.tokopedia.tkpdstream.common.network.StreamErrorResponse;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * @author by nisie on 2/1/18.
 */

@StreamScope
@Module
public class StreamNetModule {

    @StreamScope
    @Provides
    public StreamErrorInterceptor provideStreamErrorInterceptor() {
        return new StreamErrorInterceptor(StreamErrorResponse.class);
    }

    @StreamScope
    @Provides
    public AccountsAuthorizationInterceptor provideAccountsAuthorizationInterceptor(UserSession
                                                                                            userSession) {
        return new AccountsAuthorizationInterceptor(userSession);
    }

    @StreamScope
    @Provides
    public ChuckInterceptor provideChuckInterceptor(@ApplicationContext Context context) {
        return new ChuckInterceptor(context).showNotification(true);
    }


    @StreamScope
    @Provides
    public OkHttpClient provideOkHttpClient(ChuckInterceptor chuckInterceptor,
                                            HttpLoggingInterceptor httpLoggingInterceptor,
                                            TkpdAuthInterceptor tkpdAuthInterceptor,
                                            AccountsAuthorizationInterceptor accountsAuthorizationInterceptor) {
        return new OkHttpClient.Builder()
                .addInterceptor(chuckInterceptor)
                .addInterceptor(httpLoggingInterceptor)
                .addInterceptor(new ErrorResponseInterceptor(StreamErrorResponse.class))
                .addInterceptor(tkpdAuthInterceptor)
                .addInterceptor(accountsAuthorizationInterceptor)
                .build();
    }


}
