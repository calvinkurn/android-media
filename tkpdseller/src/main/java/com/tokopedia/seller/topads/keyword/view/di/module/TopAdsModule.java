package com.tokopedia.seller.topads.keyword.view.di.module;

import com.tokopedia.core.network.di.qualifier.TopAdsQualifier;
import com.tokopedia.seller.topads.keyword.view.data.source.cloud.api.KeywordApi;
import com.tokopedia.seller.topads.keyword.view.di.scope.TopAdsKeywordScope;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * Created by hendry on 5/18/2017.
 */

@TopAdsKeywordScope
@Module
public class TopAdsModule {

    @TopAdsKeywordScope
    @Provides
    KeywordApi provideKeywordApi(@TopAdsQualifier Retrofit retrofit){
        return retrofit.create(KeywordApi.class);
    }

}
