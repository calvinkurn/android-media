package com.tokopedia.home.beranda.di.module;

import android.content.Context;

import com.google.gson.Gson;
import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.network.apiservices.etc.apis.home.CategoryApi;
import com.tokopedia.home.beranda.data.mapper.TickerMapper;
import com.tokopedia.home.beranda.data.repository.HomeRepository;
import com.tokopedia.home.beranda.data.source.TickerDataSource;
import com.tokopedia.home.beranda.domain.interactor.GetTickerUseCase;

import dagger.Module;
import dagger.Provides;

/**
 * @author by errysuprayogi on 11/28/17.
 */

@Module
public class TickerModule {

    @Provides
    TickerDataSource tickerDataSource(@ApplicationContext Context context, CategoryApi categoryApi,
                                      TickerMapper tickerMapper, GlobalCacheManager cacheManager, Gson gson){
        return new TickerDataSource(context, categoryApi, tickerMapper, cacheManager, gson);
    }

    @Provides
    TickerMapper tickerMapper(){
        return new TickerMapper();
    }

    @Provides
    GetTickerUseCase getTickerUseCase(HomeRepository homeRepository){
        return new GetTickerUseCase(homeRepository);
    }
}
