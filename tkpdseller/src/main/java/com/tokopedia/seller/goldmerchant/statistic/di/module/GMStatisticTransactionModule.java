package com.tokopedia.seller.goldmerchant.statistic.di.module;

import com.tokopedia.core.network.di.qualifier.GoldMerchantQualifier;
import com.tokopedia.seller.goldmerchant.statistic.data.source.cloud.api.GMStatisticTransactionApi;
import com.tokopedia.seller.goldmerchant.statistic.di.scope.GMStatisticTransactionScope;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * @author normansyahputa on 7/6/17.
 */
@GMStatisticTransactionScope
@Module
public class GMStatisticTransactionModule {

    @GMStatisticTransactionScope
    @Provides
    GMStatisticTransactionApi provideGmStatisticTransactionApi(@GoldMerchantQualifier Retrofit retrofit) {
        return retrofit.create(GMStatisticTransactionApi.class);
    }
}
