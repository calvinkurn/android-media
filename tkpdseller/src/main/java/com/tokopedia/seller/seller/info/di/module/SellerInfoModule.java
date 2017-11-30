package com.tokopedia.seller.seller.info.di.module;

import com.tokopedia.core.network.di.qualifier.SellerInfoQualifier;
import com.tokopedia.seller.seller.info.data.source.SellerInfoApi;
import com.tokopedia.seller.seller.info.di.scope.SellerInfoScope;

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
    public SellerInfoApi provideSellerInfoApi(@SellerInfoQualifier Retrofit retrofit){
        return retrofit.create(SellerInfoApi.class);
    }
}
