package com.tokopedia.seller.gmsubscribe.di.module;

import android.content.Context;

import com.tokopedia.core.base.di.qualifier.ActivityContext;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.network.di.qualifier.CartQualifier;
import com.tokopedia.core.network.di.qualifier.GoldMerchantQualifier;
import com.tokopedia.core.network.di.qualifier.WsV4Qualifier;
import com.tokopedia.seller.gmsubscribe.data.factory.GmSubscribeCartFactory;
import com.tokopedia.seller.gmsubscribe.data.factory.GmSubscribeProductFactory;
import com.tokopedia.seller.gmsubscribe.data.repository.GmSubscribeCartRepositoryImpl;
import com.tokopedia.seller.gmsubscribe.data.repository.GmSubscribeProductRepositoryImpl;
import com.tokopedia.seller.gmsubscribe.data.source.cart.cloud.api.GmSubscribeCartApi;
import com.tokopedia.seller.gmsubscribe.data.source.product.cloud.api.GoldMerchantApi;
import com.tokopedia.seller.gmsubscribe.di.scope.GmSubscribeScope;
import com.tokopedia.seller.gmsubscribe.domain.cart.GmSubscribeCartRepository;
import com.tokopedia.seller.gmsubscribe.domain.product.GmSubscribeProductRepository;
import com.tokopedia.seller.product.edit.data.repository.ShopInfoRepositoryImpl;
import com.tokopedia.seller.product.edit.data.source.ShopInfoDataSource;
import com.tokopedia.seller.product.edit.data.source.cloud.api.ShopApi;
import com.tokopedia.seller.product.edit.domain.ShopInfoRepository;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * @author sebastianuskh on 5/2/17.
 */
@GmSubscribeScope
@Module
public class GmSubscribeModule {

    @GmSubscribeScope
    @Provides
    ShopInfoRepository provideShopInfoRepository(@ActivityContext Context context, ShopInfoDataSource shopInfoDataSource){
        return new ShopInfoRepositoryImpl(context, shopInfoDataSource);
    }

    @GmSubscribeScope
    @Provides
    GmSubscribeProductRepository provideGmSubscribeProductRepository(GmSubscribeProductFactory gmSubscribeProductFactory){
        return new GmSubscribeProductRepositoryImpl(gmSubscribeProductFactory);
    }

    @GmSubscribeScope
    @Provides
    GmSubscribeCartRepository provideGmSubscribeCartRepository(GmSubscribeCartFactory gmSubscribeCartFactory){
        return new GmSubscribeCartRepositoryImpl(gmSubscribeCartFactory);
    }

    @GmSubscribeScope
    @Provides
    GlobalCacheManager provideGlobalCacheManager(){
        return new GlobalCacheManager();
    }

    @GmSubscribeScope
    @Provides
    GmSubscribeCartApi provideGmSubscribeCartApi(@CartQualifier Retrofit retrofit){
        return retrofit.create(GmSubscribeCartApi.class);
    }

    @GmSubscribeScope
    @Provides
    GoldMerchantApi provideGoldMerchantApi(@GoldMerchantQualifier Retrofit retrofit){
        return retrofit.create(GoldMerchantApi.class);
    }

    @GmSubscribeScope
    @Provides
    ShopApi provideShopApi(@WsV4Qualifier Retrofit retrofit){
        return retrofit.create(ShopApi.class);
    }

}
