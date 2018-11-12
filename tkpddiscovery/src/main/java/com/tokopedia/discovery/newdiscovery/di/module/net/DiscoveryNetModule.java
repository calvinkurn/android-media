package com.tokopedia.discovery.newdiscovery.di.module.net;

import com.tokopedia.discovery.newdiscovery.constant.DiscoveryBaseURL;
import com.tokopedia.discovery.newdiscovery.di.qualifier.AutoCompleteQualifier;
import com.tokopedia.discovery.newdiscovery.di.qualifier.NoAuth;
import com.tokopedia.discovery.newdiscovery.di.scope.DiscoveryScope;
import com.tokopedia.discovery.newdiscovery.network.BrowseApi;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

@Module(includes = DiscoveryOkHttpClientModule.class)
public class DiscoveryNetModule {
    @DiscoveryScope
    @AutoCompleteQualifier
    @Provides
    BrowseApi provideSearchAutoCompleteApi(Retrofit.Builder builder,
                               @NoAuth OkHttpClient okHttpClient) {
        return builder.baseUrl(DiscoveryBaseURL.Ace.ACE_DOMAIN)
                .client(okHttpClient)
                .build()
                .create(BrowseApi.class);
    }
}
