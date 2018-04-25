package com.tokopedia.discovery.imagesearch.di.module;

import android.content.Context;

import com.apollographql.apollo.ApolloClient;
import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.network.apiservices.ace.apis.BrowseApi;
import com.tokopedia.core.network.apiservices.mojito.apis.MojitoApi;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.di.qualifier.DefaultAuthWithErrorHandler;
import com.tokopedia.core.network.di.qualifier.MojitoGetWishlistQualifier;
import com.tokopedia.discovery.imagesearch.data.repository.ImageSearchRepository;
import com.tokopedia.discovery.imagesearch.data.repository.ImageSearchRepositoryImpl;
import com.tokopedia.discovery.imagesearch.data.source.ImageSearchDataSource;
import com.tokopedia.discovery.imagesearch.domain.usecase.GetImageSearchUseCase;
import com.tokopedia.discovery.imagesearch.search.ImageSearchPresenter;
import com.tokopedia.discovery.newdiscovery.data.mapper.ProductMapper;
import com.tokopedia.discovery.newdiscovery.di.scope.SearchScope;
import com.tokopedia.discovery.newdiscovery.domain.usecase.GetProductUseCase;
import com.tokopedia.discovery.newdiscovery.search.SearchPresenter;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;

/**
 * Created by sachinbansal on 1/10/18.
 */

@Module
public class ImageSearchModule {

    @Provides
    ImageSearchRepository imageSearchRepository(ImageSearchDataSource imageSearchDataSource) {
        return new ImageSearchRepositoryImpl(imageSearchDataSource);
    }

    @Provides
    GetImageSearchUseCase getImageSearchUseCase(
            @ApplicationContext Context context,
            ThreadExecutor threadExecutor,
            PostExecutionThread postExecutionThread,
            ImageSearchRepository imageSearchRepository,
            @MojitoGetWishlistQualifier MojitoApi service) {
        return new GetImageSearchUseCase(context, threadExecutor,
                postExecutionThread, imageSearchRepository, service);
    }

    @Provides
    ImageSearchDataSource imageSearchDataSource(ProductMapper productMapper, BrowseApi browseApi) {
        return new ImageSearchDataSource(productMapper, browseApi);
    }

    @SearchScope
    @Provides
    ImageSearchPresenter provideImageSearchPresenter(GetProductUseCase getProductUseCase, GetImageSearchUseCase getImageSearchUseCase) {
        return new ImageSearchPresenter(getProductUseCase, getImageSearchUseCase);
    }
}
