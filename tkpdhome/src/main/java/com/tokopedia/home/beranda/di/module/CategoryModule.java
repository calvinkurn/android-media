package com.tokopedia.home.beranda.di.module;

import com.google.gson.Gson;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.network.apiservices.mojito.apis.MojitoApi;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.home.beranda.data.mapper.HomeCategoryMapper;
import com.tokopedia.home.beranda.data.repository.HomeRepository;
import com.tokopedia.home.beranda.data.source.HomeCategoryDataSource;
import com.tokopedia.home.beranda.domain.interactor.GetHomeCategoryUseCase;

import dagger.Module;
import dagger.Provides;

/**
 * @author by errysuprayogi on 11/28/17.
 */

@Module
public class CategoryModule {

    @Provides
    GetHomeCategoryUseCase getHomeCategoryUseCase(HomeRepository homeRepository){
        return new GetHomeCategoryUseCase(homeRepository);
    }

    @Provides
    HomeCategoryDataSource homeCategoryDataSource(MojitoApi mojitoApi,
                                                  HomeCategoryMapper homeCategoryMapper,
                                                  SessionHandler sessionHandler,
                                                  GlobalCacheManager cacheManager,
                                                  Gson gson){
        return new HomeCategoryDataSource(mojitoApi, homeCategoryMapper, sessionHandler, cacheManager, gson);
    }

    @Provides
    HomeCategoryMapper homeCategoryMapper(Gson gson){
        return new HomeCategoryMapper(gson);
    }

}
