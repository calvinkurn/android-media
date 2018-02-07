package com.tokopedia.gm.common.logout.di.module;

import android.content.Context;

import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.network.di.qualifier.GoldMerchantQualifier;
import com.tokopedia.core.network.di.qualifier.WsV4Qualifier;
import com.tokopedia.gm.common.logout.di.scope.GMLogoutScope;
import com.tokopedia.gm.statistic.data.repository.GMStatRepositoryImpl;
import com.tokopedia.gm.statistic.data.source.GMStatDataSource;
import com.tokopedia.gm.statistic.data.source.cloud.api.GMStatApi;
import com.tokopedia.gm.statistic.domain.GMStatRepository;
import com.tokopedia.gm.statistic.domain.interactor.GMStatClearCacheUseCase;
import com.tokopedia.gm.statistic.domain.mapper.GMTransactionStatDomainMapper;
import com.tokopedia.gm.statistic.domain.mapper.GMTransactionTableMapper;
import com.tokopedia.seller.shop.common.domain.repository.ShopInfoRepositoryImpl;
import com.tokopedia.seller.shop.common.data.source.ShopInfoDataSource;
import com.tokopedia.seller.shop.common.data.source.cloud.api.ShopApi;
import com.tokopedia.seller.shop.common.domain.repository.ShopInfoRepository;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * @author sebastianuskh on 5/8/17.
 */

@GMLogoutScope
@Module
public class GMLogoutModule {

    @GMLogoutScope
    @Provides
    ShopInfoRepository provideShopInfoRepository(@ApplicationContext Context context, ShopInfoDataSource shopInfoDataSource) {
        return new ShopInfoRepositoryImpl(context, shopInfoDataSource);
    }

    @GMLogoutScope
    @Provides
    ShopApi provideShopApi(@WsV4Qualifier Retrofit retrofit){
        return retrofit.create(ShopApi.class);
    }

    @GMLogoutScope
    @Provides
    GMStatRepository provideGMStatRepository(GMStatDataSource gmStatDataSource,
                                             GMTransactionStatDomainMapper gmTransactionStatDomainMapper,
                                             GMTransactionTableMapper gmTransactionTableMapper,
                                             ShopInfoRepository shopInfoRepository) {
        return new GMStatRepositoryImpl(gmTransactionStatDomainMapper, gmStatDataSource, gmTransactionTableMapper,
                shopInfoRepository);
    }

    @GMLogoutScope
    @Provides
    GMStatApi provideGmStatisticTransactionApi(@GoldMerchantQualifier Retrofit retrofit) {
        return retrofit.create(GMStatApi.class);
    }

    @GMLogoutScope
    @Provides
    GMStatClearCacheUseCase provideGMStatClearCacheUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread, GMStatRepository gmStatRepository){
        return new GMStatClearCacheUseCase(threadExecutor, postExecutionThread, gmStatRepository);
    }

}
