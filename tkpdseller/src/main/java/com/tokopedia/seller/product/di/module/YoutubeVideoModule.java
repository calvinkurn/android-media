package com.tokopedia.seller.product.di.module;

import com.tokopedia.core.base.di.scope.ActivityScope;
import com.tokopedia.core.network.di.qualifier.YoutubeQualifier;
import com.tokopedia.seller.product.data.source.cloud.api.YoutubeVideoLinkApi;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * @author normansyahputa on 4/11/17.
 */
@ActivityScope
@Module
public class YoutubeVideoModule {
    @ActivityScope
    @Provides
    YoutubeVideoLinkApi provideYoutubeVideoLinkApi(@YoutubeQualifier Retrofit retrofit) {
        return retrofit.create(YoutubeVideoLinkApi.class);
    }
}
