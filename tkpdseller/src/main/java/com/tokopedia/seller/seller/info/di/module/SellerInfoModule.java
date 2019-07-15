package com.tokopedia.seller.seller.info.di.module;

import android.content.Context;

import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.core.network.di.qualifier.WsV4Qualifier;
import com.tokopedia.seller.R;
import com.tokopedia.seller.seller.info.data.repository.SellerInfoRepositoryImpl;
import com.tokopedia.seller.seller.info.data.source.SellerInfoApi;
import com.tokopedia.seller.seller.info.data.source.cloud.SellerInfoDataSource;
import com.tokopedia.seller.seller.info.di.scope.SellerInfoScope;
import com.tokopedia.seller.seller.info.domain.SellerInfoRepository;

import javax.inject.Named;

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


    @Named("SELLER_CENTER_RAW")
    @Provides
    public String provideRawSellerCenter(@ApplicationContext Context context){
        return GraphqlHelper.loadRawString(context.getResources(), R.raw.gold_merchant_status);
    }
}
