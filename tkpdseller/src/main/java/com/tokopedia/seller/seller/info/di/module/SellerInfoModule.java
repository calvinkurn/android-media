package com.tokopedia.seller.seller.info.di.module;

import com.tokopedia.core.network.di.qualifier.WsV4Qualifier;
import com.tokopedia.seller.seller.info.data.repository.SellerInfoRepositoryImpl;
import com.tokopedia.seller.seller.info.data.source.SellerInfoApi;
import com.tokopedia.seller.seller.info.data.source.cloud.SellerInfoDataSource;
import com.tokopedia.seller.seller.info.di.scope.SellerInfoScope;
import com.tokopedia.seller.seller.info.domain.SellerInfoRepository;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * Created by normansyahputa on 11/30/17.
 */
@SellerInfoScope
@Module
public class SellerInfoModule {

    @SellerInfoScope
    @Provides
    public SellerInfoApi provideSellerInfoApi(@WsV4Qualifier Retrofit retrofit) {
        return retrofit.create(SellerInfoApi.class);
    }

    @SellerInfoScope
    @Provides
    public SellerInfoRepository provideSellerInfoRepository(SellerInfoDataSource sellerInfoDataSource){
        return new SellerInfoRepositoryImpl(sellerInfoDataSource);
    }
}
