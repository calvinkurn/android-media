package com.tokopedia.discovery.newdiscovery.di.module;

import android.content.Context;

import com.google.gson.Gson;
import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.network.apiservices.ace.apis.BrowseApi;
import com.tokopedia.core.network.apiservices.mojito.MojitoNoRetryAuthService;
import com.tokopedia.core.network.apiservices.mojito.MojitoSimpleService;
import com.tokopedia.core.network.apiservices.mojito.apis.MojitoApi;
import com.tokopedia.core.network.apiservices.mojito.apis.MojitoAuthApi;
import com.tokopedia.core.network.di.qualifier.MojitoGetWishlistQualifier;
import com.tokopedia.core.network.di.qualifier.MojitoWishlistActionQualifier;
import com.tokopedia.discovery.newdiscovery.data.mapper.AddWishlistActionMapper;
import com.tokopedia.discovery.newdiscovery.data.mapper.ProductMapper;
import com.tokopedia.discovery.newdiscovery.data.mapper.RemoveWishlistActionMapper;
import com.tokopedia.discovery.newdiscovery.data.repository.BannerRepository;
import com.tokopedia.discovery.newdiscovery.data.repository.ProductRepository;
import com.tokopedia.discovery.newdiscovery.data.repository.ProductRepositoryImpl;
import com.tokopedia.discovery.newdiscovery.data.source.ProductDataSource;
import com.tokopedia.discovery.newdiscovery.domain.usecase.AddWishlistActionUseCase;
import com.tokopedia.discovery.newdiscovery.domain.usecase.GetProductUseCase;
import com.tokopedia.discovery.newdiscovery.domain.usecase.RemoveWishlistActionUseCase;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * Created by hangnadi on 10/5/17.
 */

@Module
public class ProductModule {

    @Provides
    ProductRepository productRepository(ProductDataSource productDataSource){
        return new ProductRepositoryImpl(productDataSource);
    }

    @Provides
    GetProductUseCase getProductUseCase(
            @ApplicationContext Context context,
            ThreadExecutor threadExecutor,
            PostExecutionThread postExecutionThread,
            ProductRepository productRepository,
            BannerRepository bannerRepository,
            @MojitoGetWishlistQualifier MojitoApi service) {
        return new GetProductUseCase(context, threadExecutor,
                postExecutionThread, productRepository, bannerRepository, service);
    }

    @Provides
    AddWishlistActionUseCase addWishlistActionUseCase(
            ThreadExecutor threadExecutor,
            PostExecutionThread postExecutionThread,
            @MojitoWishlistActionQualifier MojitoAuthApi service,
            AddWishlistActionMapper mapper) {
        return new AddWishlistActionUseCase(threadExecutor, postExecutionThread, service, mapper);
    }

    @Provides
    RemoveWishlistActionUseCase removeWishlistActionUseCase(
            ThreadExecutor threadExecutor,
            PostExecutionThread postExecutionThread,
            @MojitoWishlistActionQualifier MojitoAuthApi service,
            RemoveWishlistActionMapper mapper) {
        return new RemoveWishlistActionUseCase(threadExecutor, postExecutionThread, service, mapper);
    }

    @Provides
    AddWishlistActionMapper addWishlistActionMapper(){
        return new AddWishlistActionMapper();
    }

    @Provides
    RemoveWishlistActionMapper removeWishlistActionMapper(){
        return new RemoveWishlistActionMapper();
    }

    @Provides
    ProductDataSource productDataSource(BrowseApi searchApi, ProductMapper productMapper) {
        return new ProductDataSource(searchApi, productMapper);
    }

    @Provides
    ProductMapper productMapper(Gson gson) {
        return new ProductMapper(gson);
    }
}
