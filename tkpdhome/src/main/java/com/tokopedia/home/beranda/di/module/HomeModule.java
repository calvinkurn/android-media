package com.tokopedia.home.beranda.di.module;

import android.content.Context;

import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.home.beranda.data.mapper.HomeMapper;
import com.tokopedia.home.beranda.data.repository.HomeRepository;
import com.tokopedia.home.beranda.data.repository.HomeRepositoryImpl;
import com.tokopedia.home.beranda.data.source.BrandsOfficialStoreDataSource;
import com.tokopedia.home.beranda.data.source.HomeBannerDataSource;
import com.tokopedia.home.beranda.data.source.HomeCategoryDataSource;
import com.tokopedia.home.beranda.data.source.HomeDataSource;
import com.tokopedia.home.beranda.data.source.TickerDataSource;
import com.tokopedia.home.beranda.data.source.TopPicksDataSource;
import com.tokopedia.home.beranda.data.source.api.HomeDataApi;
import com.tokopedia.home.beranda.data.source.api.HomeDataService;
import com.tokopedia.home.beranda.di.HomeScope;
import com.tokopedia.home.beranda.domain.interactor.GetHomeDataUseCase;
import com.tokopedia.home.beranda.presentation.presenter.HomePresenter;

import dagger.Module;
import dagger.Provides;

/**
 * @author by errysuprayogi on 11/28/17.
 */

@Module(includes = {CategoryModule.class, BannerModule.class, BrandsModule.class, TopPicksModule.class, TickerModule.class, HomeFeedModule.class})
public class HomeModule {

    @HomeScope
    @Provides
    HomeMapper providehomeMapper(){
        return new HomeMapper();
    }

    @HomeScope
    @Provides
    GlobalCacheManager globalCacheManager() {
        return new GlobalCacheManager();
    }

    @HomeScope
    @Provides
    HomePresenter homePresenter(@ApplicationContext Context context) {
        return new HomePresenter(context);
    }

    @HomeScope
    @Provides
    HomeRepository homeRepository(HomeCategoryDataSource homeCategoryDataSource,
                                  HomeBannerDataSource homeBannerDataSource,
                                  BrandsOfficialStoreDataSource brandsOfficialStoreDataSource,
                                  TopPicksDataSource topPicksDataSource,
                                  TickerDataSource tickerDataSource,
                                  HomeDataSource homeDataSource) {
        return new HomeRepositoryImpl(homeCategoryDataSource, homeBannerDataSource,
                brandsOfficialStoreDataSource, topPicksDataSource, tickerDataSource, homeDataSource);
    }

    @HomeScope
    @Provides
    HomeDataService provideHomeDataService() {
        return new HomeDataService();
    }

    @HomeScope
    @Provides
    HomeDataApi provideHomeDataApi(HomeDataService service) {
        return service.getApi();
    }

    @Provides
    HomeDataSource provideHomeDataSource(HomeDataApi homeDataApi,
                                         HomeMapper homeMapper,
                                         @ApplicationContext Context context){
        return new HomeDataSource(homeDataApi, homeMapper, context);
    }

    @Provides
    GetHomeDataUseCase provideGetHomeDataUseCase(HomeRepository homeRepository){
        return new GetHomeDataUseCase(homeRepository);
    }
}
