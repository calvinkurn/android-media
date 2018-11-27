package com.tokopedia.discovery.imagesearch.di.module;

import android.content.Context;

import com.google.gson.Gson;
import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.network.apiservices.mojito.apis.MojitoApi;
import com.tokopedia.core.network.di.qualifier.MojitoGetWishlistQualifier;
import com.tokopedia.discovery.imagesearch.data.mapper.ImageProductMapper;
import com.tokopedia.discovery.imagesearch.domain.usecase.GetImageSearchUseCase;
import com.tokopedia.discovery.imagesearch.network.apiservice.ImageSearchService;
import com.tokopedia.discovery.imagesearch.search.ImageSearchPresenter;
import com.tokopedia.discovery.newdiscovery.data.mapper.ProductMapper;
import com.tokopedia.discovery.newdiscovery.di.scope.SearchScope;
import com.tokopedia.discovery.newdiscovery.domain.usecase.GetProductUseCase;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import dagger.Module;
import dagger.Provides;

/**
 * Created by sachinbansal on 1/10/18.
 */

@Module
public class ImageSearchModule {

    @Provides
    ImageSearchService imageSearchService() {
        return new ImageSearchService();
    }

    @Provides
    GetImageSearchUseCase getImageSearchUseCase(
            @ApplicationContext Context context,
            ThreadExecutor threadExecutor,
            PostExecutionThread postExecutionThread,
            ImageSearchService imageSearchService,
            ImageProductMapper imageProductMapper,
            @MojitoGetWishlistQualifier MojitoApi service,
            UserSessionInterface userSession) {
        return new GetImageSearchUseCase(context, threadExecutor,
                postExecutionThread, imageSearchService, imageProductMapper, service, userSession);
    }

    @Provides
    ImageProductMapper imageProductMapper(Gson gson, ProductMapper productMapper) {
        return new ImageProductMapper(gson, productMapper);
    }

    @SearchScope
    @Provides
    ImageSearchPresenter provideImageSearchPresenter(@ApplicationContext Context context, GetProductUseCase getProductUseCase, GetImageSearchUseCase getImageSearchUseCase) {
        return new ImageSearchPresenter(context, getProductUseCase, getImageSearchUseCase);
    }

    @Provides
    UserSessionInterface provideUserSessionInterface(@ApplicationContext Context context) {
        return new UserSession(context);
    }
}
