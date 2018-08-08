package com.tokopedia.discovery.newdiscovery.di.module;

import com.google.gson.Gson;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.network.apiservices.mojito.apis.MojitoApi;
import com.tokopedia.core.network.apiservices.search.apis.HotListApi;
import com.tokopedia.discovery.newdiscovery.data.mapper.OfficialStoreBannerMapper;
import com.tokopedia.discovery.newdiscovery.data.repository.BannerRepositoryImpl;
import com.tokopedia.discovery.newdiscovery.hotlist.data.mapper.HotlistBannerMapper;
import com.tokopedia.discovery.newdiscovery.data.repository.BannerRepository;
import com.tokopedia.discovery.newdiscovery.data.source.BannerDataSource;
import com.tokopedia.discovery.newdiscovery.hotlist.domain.usecase.GetHotlistBannerUseCase;

import dagger.Module;
import dagger.Provides;

/**
 * Created by hangnadi on 10/6/17.
 */

@Module
public class BannerModule {

    @Provides
    BannerRepository bannerRepository(BannerDataSource bannerDataSource){
        return new BannerRepositoryImpl(bannerDataSource);
    }

    @Provides
    GetHotlistBannerUseCase getHotlistBannerUseCase(
            ThreadExecutor threadExecutor,
            PostExecutionThread postExecutionThread,
            BannerRepository bannerRepository) {
        return new GetHotlistBannerUseCase(threadExecutor, postExecutionThread, bannerRepository);
    }

    @Provides
    BannerDataSource bannerDataSource(HotListApi hotListApi, MojitoApi mojitoApi,
                                      HotlistBannerMapper hotlistBannerMapper,
                                      OfficialStoreBannerMapper officialStoreBannerMapper) {
        return new BannerDataSource(hotListApi, mojitoApi, hotlistBannerMapper, officialStoreBannerMapper);
    }

    @Provides
    OfficialStoreBannerMapper officialStoreBannerMapper(Gson gson){
        return new OfficialStoreBannerMapper(gson);
    }

    @Provides
    HotlistBannerMapper botlistBannerMapper(Gson gson) {
        return new HotlistBannerMapper(gson);
    }
}
