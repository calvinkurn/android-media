package com.tokopedia.seller.shop.setting.di.module;

import com.tokopedia.core.network.di.qualifier.WsV4Qualifier;
import com.tokopedia.seller.shop.open.data.source.cloud.api.OpenShopApi;
import com.tokopedia.seller.shop.setting.data.repository.DistrictLogisticDataRepositoryImpl;
import com.tokopedia.seller.shop.setting.data.source.DistrictDataSource;
import com.tokopedia.seller.shop.setting.data.source.LogisticDataSource;
import com.tokopedia.seller.shop.setting.di.scope.ShopSettingScope;
import com.tokopedia.seller.shop.setting.domain.DistrictLogisticDataRepository;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * @author sebastianuskh on 3/20/17.
 */
@ShopSettingScope
@Module
public class ShopSettingModule {

    @Provides
    @ShopSettingScope
    public DistrictLogisticDataRepository provideDistrictLogisticDataRepository(DistrictDataSource districtDataSource, LogisticDataSource logisticDataSource) {
        return new DistrictLogisticDataRepositoryImpl(districtDataSource, logisticDataSource);
    }

    @Provides
    @ShopSettingScope
    public OpenShopApi provideOpenShopApi(@WsV4Qualifier Retrofit retrofit) {
        return retrofit.create(OpenShopApi.class);
    }
}