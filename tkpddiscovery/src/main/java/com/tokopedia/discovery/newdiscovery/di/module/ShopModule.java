package com.tokopedia.discovery.newdiscovery.di.module;

import android.content.Context;

import com.google.gson.Gson;
import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.network.apiservices.ace.apis.BrowseApi;
import com.tokopedia.core.network.apiservices.tome.TomeService;
import com.tokopedia.core.shopinfo.facades.authservices.ActionService;
import com.tokopedia.discovery.newdiscovery.data.mapper.ShopMapper;
import com.tokopedia.discovery.newdiscovery.data.mapper.ToggleFavoriteActionMapper;
import com.tokopedia.discovery.newdiscovery.data.repository.ShopRepository;
import com.tokopedia.discovery.newdiscovery.data.repository.ShopRepositoryImpl;
import com.tokopedia.discovery.newdiscovery.data.source.ShopDataSource;
import com.tokopedia.discovery.newdiscovery.domain.usecase.GetShopUseCase;
import com.tokopedia.discovery.newdiscovery.domain.usecase.ToggleFavoriteActionUseCase;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.shop.common.domain.interactor.ToggleFavouriteShopUseCase;

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
    ToggleFavouriteShopUseCase toggleFavouriteShopUseCase(
            @ApplicationContext Context context) {
        return new ToggleFavouriteShopUseCase(new GraphqlUseCase(), context.getResources());
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
    TomeService tomeService() {
        return new TomeService();
    }

    @Provides
    ActionService actionService() {
        return new ActionService();
    }

    @Provides
    ToggleFavoriteActionMapper toggleFavoriteActionMapper() {
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
    ShopRepository shopRepository(ShopDataSource shopDataSource) {
        return new ShopRepositoryImpl(shopDataSource);
    }
}
