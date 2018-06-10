package com.tokopedia.home.explore.di;

import android.content.Context;

import com.google.gson.Gson;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.drawer2.data.mapper.ProfileMapper;
import com.tokopedia.core.drawer2.data.source.CloudProfileSource;
import com.tokopedia.core.network.apiservices.user.PeopleService;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.home.common.HomeDataApi;
import com.tokopedia.home.explore.data.repository.ExploreRepositoryImpl;
import com.tokopedia.home.explore.data.source.ExploreDataSource;
import com.tokopedia.home.explore.domain.GetExploreDataUseCase;
import com.tokopedia.home.explore.domain.GetExploreLocalDataUseCase;
import com.tokopedia.home.explore.view.presentation.ExplorePresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by errysuprayogi on 2/2/18.
 */

@ExploreScope
@Module
public class ExploreModule {

    @Provides
    ExplorePresenter explorePresenter() {
        return new ExplorePresenter();
    }

    @ExploreScope
    @Provides
    GlobalCacheManager cacheManager() {
        return new GlobalCacheManager();
    }

    @ExploreScope
    @Provides
    ExploreDataSource dataSource(@ApplicationContext Context context, HomeDataApi dataApi,
                                 GlobalCacheManager cacheManager, CloudProfileSource profileSource, Gson gson) {
        return new ExploreDataSource(context, dataApi, cacheManager, profileSource, gson);
    }

    @ExploreScope
    @Provides
    ExploreRepositoryImpl exploreRepository(@ApplicationContext Context context, ExploreDataSource dataSource) {
        return new ExploreRepositoryImpl(context, dataSource);
    }

    @ExploreScope
    @Provides
    GetExploreDataUseCase getExploreDataUseCase(ExploreRepositoryImpl repository, @ApplicationContext Context context) {
        return new GetExploreDataUseCase(repository, context);
    }

    @ExploreScope
    @Provides
    GetExploreLocalDataUseCase getExploreLocalDataUseCase(ExploreRepositoryImpl repository) {
        return new GetExploreLocalDataUseCase(repository);
    }


    @ExploreScope
    @Provides
    CloudProfileSource cloudProfileSource(@ApplicationContext Context context) {
        return new CloudProfileSource(context, new PeopleService(), new ProfileMapper(),
                new GlobalCacheManager(), new SessionHandler(context));
    }

}
