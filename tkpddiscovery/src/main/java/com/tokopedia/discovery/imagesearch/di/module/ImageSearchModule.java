package com.tokopedia.discovery.imagesearch.di.module;

import android.content.Context;

import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.network.apiservices.mojito.apis.MojitoApi;
import com.tokopedia.core.network.di.qualifier.MojitoGetWishlistQualifier;
import com.tokopedia.discovery.imagesearch.domain.usecase.GetImageSearchUseCase;
import com.tokopedia.discovery.imagesearch.network.apiservice.ImageSearchService;
import com.tokopedia.discovery.imagesearch.search.ImageSearchPresenter;
import com.tokopedia.discovery.newdiscovery.data.mapper.ProductMapper;
import com.tokopedia.discovery.newdiscovery.di.module.ProductModule;
import com.tokopedia.discovery.newdiscovery.di.scope.SearchScope;
import com.tokopedia.discovery.newdiscovery.domain.usecase.GetProductUseCase;

import dagger.Module;
import dagger.Provides;

/**
 * Created by sachinbansal on 1/10/18.
 */

@Module(includes = ProductModule.class)
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
            ProductMapper productMapper,
            @MojitoGetWishlistQualifier MojitoApi service) {
        return new GetImageSearchUseCase(context, threadExecutor,
                postExecutionThread, imageSearchService, productMapper, service);
    }

    @SearchScope
    @Provides
    ImageSearchPresenter provideImageSearchPresenter(GetProductUseCase getProductUseCase, GetImageSearchUseCase getImageSearchUseCase) {
        return new ImageSearchPresenter(getProductUseCase, getImageSearchUseCase);
    }
}
