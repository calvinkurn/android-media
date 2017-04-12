package com.tokopedia.seller.product.di.module;

import com.tokopedia.core.base.di.scope.ActivityScope;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.network.di.qualifier.YoutubeQualifier;
import com.tokopedia.seller.product.data.mapper.YoutubeVidToDomainMapper;
import com.tokopedia.seller.product.data.repository.YoutubeRepositoryImpl;
import com.tokopedia.seller.product.data.source.YoutubeVideoLinkDataSource;
import com.tokopedia.seller.product.data.source.cloud.YoutubeVideoLinkCloud;
import com.tokopedia.seller.product.data.source.cloud.api.YoutubeVideoLinkApi;
import com.tokopedia.seller.product.domain.YoutubeVideoRepository;
import com.tokopedia.seller.product.domain.interactor.YoutubeVideoUseCase;

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
    YoutubeVideoUseCase provideYoutubeVideoUseCase(
            ThreadExecutor threadExecutor,
            PostExecutionThread postExecutionThread,
            YoutubeVideoRepository youtubeVideoRepository
    ) {
        return new YoutubeVideoUseCase(
                threadExecutor, postExecutionThread, youtubeVideoRepository
        );
    }

    @ActivityScope
    @Provides
    YoutubeVideoRepository provideYoutubeVideoRepository(
            YoutubeVideoLinkDataSource youtubeVideoLinkDataSource) {
        return new YoutubeRepositoryImpl(youtubeVideoLinkDataSource);
    }

    @ActivityScope
    @Provides
    YoutubeVideoLinkDataSource provideYoutubeVideoLinkDataSource(
            YoutubeVideoLinkCloud youtubeVideoLinkCloud,
            YoutubeVidToDomainMapper youtubeVidToDomainMapper
    ) {
        return new YoutubeVideoLinkDataSource(
                youtubeVideoLinkCloud, youtubeVidToDomainMapper
        );
    }

    @ActivityScope
    @Provides
    YoutubeVidToDomainMapper provideYoutubeVidToDomainMapper() {
        return new YoutubeVidToDomainMapper();
    }

    @ActivityScope
    @Provides
    YoutubeVideoLinkCloud provideYoutubeVideoLinkCloud(
            YoutubeVideoLinkApi youtubeVideoLinkApi) {
        return new YoutubeVideoLinkCloud(youtubeVideoLinkApi);
    }

    @ActivityScope
    @Provides
    YoutubeVideoLinkApi provideYoutubeVideoLinkApi(@YoutubeQualifier Retrofit retrofit) {
        return retrofit.create(YoutubeVideoLinkApi.class);
    }


}
