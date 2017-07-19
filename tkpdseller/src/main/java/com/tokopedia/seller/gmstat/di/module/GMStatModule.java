package com.tokopedia.seller.gmstat.di.module;

import android.content.Context;

import com.google.gson.Gson;
import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.core.network.di.qualifier.GoldMerchantQualifier;
import com.tokopedia.seller.gmstat.apis.GMStatApi;
import com.tokopedia.seller.gmstat.di.scope.GMStatScope;
import com.tokopedia.seller.gmstat.utils.GMStatNetworkController;
import com.tokopedia.seller.gmstat.utils.GMStatNetworkController2;
import com.tokopedia.seller.goldmerchant.statistic.data.repository.GMStatRepositoryImpl;
import com.tokopedia.seller.goldmerchant.statistic.data.source.GMStatDataSource;
import com.tokopedia.seller.goldmerchant.statistic.domain.GMStatRepository;
import com.tokopedia.seller.goldmerchant.statistic.domain.mapper.GMTransactionStatDomainMapper;
import com.tokopedia.seller.goldmerchant.statistic.domain.mapper.GMTransactionTableMapper;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * Created by normansyahputa on 6/15/17.
 */
@GMStatScope
@Module
public class GMStatModule {
    @Provides
    @GMStatScope
    public GMStatApi provideGmStatApi(@GoldMerchantQualifier Retrofit retrofit) {
        return retrofit.create(GMStatApi.class);
    }


    @GMStatScope
    @Provides
    public GMStatNetworkController provideGmStatNetworkController(@ApplicationContext Context context, Gson gson, GMStatApi gmStatApi) {
        return new GMStatNetworkController2(context, gson, gmStatApi);
    }

    @GMStatScope
    @Provides
    GMStatRepository provideGMStatRepository(GMStatDataSource gmStatDataSource,
                                             GMTransactionStatDomainMapper gmTransactionStatDomainMapper,
                                             GMTransactionTableMapper gmTransactionTableMapper) {
        return new GMStatRepositoryImpl(gmTransactionStatDomainMapper, gmStatDataSource, gmTransactionTableMapper);
    }

    @GMStatScope
    @Provides
    com.tokopedia.seller.goldmerchant.statistic.data.source.cloud.api.GMStatApi provideGmStatisticTransactionApi(@GoldMerchantQualifier Retrofit retrofit) {
        return retrofit.create(com.tokopedia.seller.goldmerchant.statistic.data.source.cloud.api.GMStatApi.class);
    }
}
