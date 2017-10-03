package com.tokopedia.seller.product.edit.di.module;

import android.content.Context;

import com.tkpd.library.utils.image.ImageHandler;
import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.core.base.di.scope.ActivityScope;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.network.di.qualifier.YoutubeQualifier;
import com.tokopedia.seller.product.edit.data.mapper.YoutubeVidToDomainMapper;
import com.tokopedia.seller.product.edit.data.repository.YoutubeRepositoryImpl;
import com.tokopedia.seller.product.edit.data.source.YoutubeVideoLinkDataSource;
import com.tokopedia.seller.product.edit.data.source.cloud.YoutubeVideoLinkCloud;
import com.tokopedia.seller.product.edit.data.source.cloud.api.YoutubeVideoLinkApi;
import com.tokopedia.seller.product.edit.domain.YoutubeVideoRepository;
import com.tokopedia.seller.product.edit.domain.interactor.YoutubeVideoUseCase;
import com.tokopedia.seller.product.edit.utils.YoutubeVideoLinkUtils;

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
    ImageHandler provideImageHandler(@ApplicationContext Context context) {
        return new ImageHandler(context);
    }

    @ActivityScope
    @Provides
    YoutubeVideoLinkUtils provideYoutubeVideoLinkUtils(@ApplicationContext Context context) {
        YoutubeVideoLinkUtils youtubeVideoLinkUtils = new YoutubeVideoLinkUtils();
        youtubeVideoLinkUtils.fillExceptionString(context);
        return youtubeVideoLinkUtils;
    }

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
            YoutubeVideoLinkApi youtubeVideoLinkApi,
            YoutubeVideoLinkUtils youtubeVideoLinkUtils
    ) {
        return new YoutubeVideoLinkCloud(youtubeVideoLinkApi, youtubeVideoLinkUtils);
    }

    @ActivityScope
    @Provides
    YoutubeVideoLinkApi provideYoutubeVideoLinkApi(@YoutubeQualifier Retrofit retrofit) {
        return retrofit.create(YoutubeVideoLinkApi.class);
    }


}
