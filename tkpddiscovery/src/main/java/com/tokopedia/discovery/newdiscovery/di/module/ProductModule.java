package com.tokopedia.discovery.newdiscovery.di.module;

import android.content.Context;

import com.google.gson.Gson;
import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.network.apiservices.ace.apis.BrowseApi;
import com.tokopedia.core.network.apiservices.mojito.apis.MojitoApi;
import com.tokopedia.core.network.di.qualifier.MojitoGetWishlistQualifier;
import com.tokopedia.discovery.newdiscovery.data.mapper.ProductMapper;
import com.tokopedia.discovery.newdiscovery.data.repository.BannerRepository;
import com.tokopedia.discovery.newdiscovery.data.repository.ProductRepository;
import com.tokopedia.discovery.newdiscovery.data.repository.ProductRepositoryImpl;
import com.tokopedia.discovery.newdiscovery.data.source.ProductDataSource;
import com.tokopedia.discovery.newdiscovery.domain.usecase.GetProductUseCase;
import com.tokopedia.wishlist.common.usecase.TkpdAddWishListUseCase;
import com.tokopedia.wishlist.common.usecase.TkpdRemoveWishListUseCase;

import dagger.Module;
import dagger.Provides;

/**
 * Created by hangnadi on 10/5/17.
 */

@Module
public class ProductModule {

    @Provides
    ProductRepository productRepository(ProductDataSource productDataSource) {
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

    /*@Provides
    AddWishlistActionUseCase addWishlistActionUseCase(
            @ApplicationContext Context context) {
        return new AddWishlistActionUseCase(context);
    }*/

    @Provides
    TkpdAddWishListUseCase providesTkpdAddWishListUseCase(
            @ApplicationContext Context context) {
        return new TkpdAddWishListUseCase(context);
    }


    @Provides
    TkpdRemoveWishListUseCase providesTkpdRemoveWishListUseCase(
            @ApplicationContext Context context) {
        return new TkpdRemoveWishListUseCase(context);
    }

    /*@Provides
    RemoveWishlistActionUseCase removeWishlistActionUseCase(
            @ApplicationContext Context context) {
        return new RemoveWishlistActionUseCase(context);
    }*/

    /*@Provides
    AddWishlistActionMapper addWishlistActionMapper() {
        return new AddWishlistActionMapper();
    }*/

    /*@Provides
    RemoveWishlistActionMapper removeWishlistActionMapper() {
        return new RemoveWishlistActionMapper();
    }*/

    @Provides
    ProductDataSource productDataSource(BrowseApi searchApi, ProductMapper productMapper) {
        return new ProductDataSource(searchApi, productMapper);
    }

    @Provides
    ProductMapper productMapper(Gson gson) {
        return new ProductMapper(gson);
    }
}
