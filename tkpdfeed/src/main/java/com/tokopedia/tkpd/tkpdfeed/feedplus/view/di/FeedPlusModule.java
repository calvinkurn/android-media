package com.tokopedia.tkpd.tkpdfeed.feedplus.view.di;

import com.apollographql.apollo.ApolloClient;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.network.di.module.OkHttpClientModule;
import com.tokopedia.core.network.di.qualifier.DefaultAuth;
import com.tokopedia.core.network.di.qualifier.DefaultAuthWithErrorHandler;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;

/**
 * @author by nisie on 5/15/17.
 */

@Module
public class FeedPlusModule {

    @FeedPlusScope
    @Provides
    GlobalCacheManager provideGlobalCacheManager() {
        return new GlobalCacheManager();
    }

    @FeedPlusScope
    @Provides
    ApolloClient providesApolloClient(@DefaultAuthWithErrorHandler OkHttpClient okHttpClient) {
        return ApolloClient.builder()
                .okHttpClient(okHttpClient)
                .serverUrl("https://m.tokopedia.com/graphql")
                .build();
    }


}
