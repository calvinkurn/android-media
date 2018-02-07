package com.tokopedia.discovery.newdiscovery.di.module;

import android.content.Context;

import com.google.gson.Gson;
import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.network.apiservices.ace.apis.BrowseApi;
import com.tokopedia.core.network.apiservices.mojito.MojitoNoRetryAuthService;
import com.tokopedia.core.network.apiservices.mojito.MojitoSimpleService;
import com.tokopedia.core.network.apiservices.tome.TomeService;
import com.tokopedia.core.shopinfo.facades.authservices.ActionService;
import com.tokopedia.discovery.newdiscovery.data.mapper.AddWishlistActionMapper;
import com.tokopedia.discovery.newdiscovery.data.mapper.ShopMapper;
import com.tokopedia.discovery.newdiscovery.data.mapper.ToggleFavoriteActionMapper;
import com.tokopedia.discovery.newdiscovery.data.repository.ProductRepository;
import com.tokopedia.discovery.newdiscovery.data.repository.ProductRepositoryImpl;
import com.tokopedia.discovery.newdiscovery.data.repository.ShopRepository;
import com.tokopedia.discovery.newdiscovery.data.repository.ShopRepositoryImpl;
import com.tokopedia.discovery.newdiscovery.data.source.ProductDataSource;
import com.tokopedia.discovery.newdiscovery.data.source.ShopDataSource;
import com.tokopedia.discovery.newdiscovery.domain.usecase.AddWishlistActionUseCase;
import com.tokopedia.discovery.newdiscovery.domain.usecase.GetShopUseCase;
import com.tokopedia.discovery.newdiscovery.domain.usecase.ToggleFavoriteActionUseCase;

import dagger.Module;
import dagger.Provides;

/**
 * Created by henrypriyono on 10/13/17.
 */

@Module
public class ShopModule {
    @Provides
    GetShopUseCase getShopUseCase(
            ThreadExecutor threadExecutor,
            PostExecutionThread postExecutionThread,
            ShopRepository shopRepository,
            TomeService tomeService) {
        return new GetShopUseCase(threadExecutor, postExecutionThread, shopRepository, tomeService);
    }

    @Provides
    ToggleFavoriteActionUseCase toggleFavoriteActionUseCase(
            ThreadExecutor threadExecutor,
            PostExecutionThread postExecutionThread,
            ActionService service,
            ToggleFavoriteActionMapper mapper,
            @ApplicationContext Context context) {
        return new ToggleFavoriteActionUseCase(threadExecutor, postExecutionThread, service, mapper, context);
    }

    @Provides
    TomeService tomeService(){
        return new TomeService();
    }

    @Provides
    ActionService actionService(){
        return new ActionService();
    }

    @Provides
    ToggleFavoriteActionMapper toggleFavoriteActionMapper(){
        return new ToggleFavoriteActionMapper();
    }

    @Provides
    ShopDataSource ShopDataSource(BrowseApi searchApi, ShopMapper shopMapper) {
        return new ShopDataSource(searchApi, shopMapper);
    }

    @Provides
    ShopMapper shopMapper(Gson gson) {
        return new ShopMapper(gson);
    }

    @Provides
    ShopRepository shopRepository(ShopDataSource shopDataSource){
        return new ShopRepositoryImpl(shopDataSource);
    }
}
