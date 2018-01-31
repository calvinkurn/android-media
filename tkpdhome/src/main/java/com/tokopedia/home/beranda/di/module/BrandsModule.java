package com.tokopedia.home.beranda.di.module;

import com.google.gson.Gson;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.network.apiservices.mojito.apis.MojitoApi;
import com.tokopedia.home.beranda.data.mapper.BrandsOfficialStoreMapper;
import com.tokopedia.home.beranda.data.repository.HomeRepository;
import com.tokopedia.home.beranda.data.source.BrandsOfficialStoreDataSource;
import com.tokopedia.home.beranda.domain.interactor.GetBrandsOfficialStoreUseCase;

import dagger.Module;
import dagger.Provides;

/**
 * @author by errysuprayogi on 11/28/17.
 */

@Module
public class BrandsModule {

    @Provides
    BrandsOfficialStoreDataSource brandsOfficialStoreDataSource(MojitoApi mojitoApi,
                                                                BrandsOfficialStoreMapper brandsOfficialStoreMapper,
                                                                GlobalCacheManager cacheManager,
                                                                Gson gson){
        return new BrandsOfficialStoreDataSource(mojitoApi, brandsOfficialStoreMapper, cacheManager, gson);
    }

    @Provides
    BrandsOfficialStoreMapper brandsOfficialStoreMapper(Gson gson){
        return new BrandsOfficialStoreMapper(gson);
    }

    @Provides
    GetBrandsOfficialStoreUseCase getBrandsOfficialStoreUseCase(HomeRepository homeRepository){
        return new GetBrandsOfficialStoreUseCase(homeRepository);
    }
}
