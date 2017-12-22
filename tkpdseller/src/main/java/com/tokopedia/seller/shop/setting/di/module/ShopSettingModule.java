package com.tokopedia.seller.shop.setting.di.module;

import com.tokopedia.core.network.di.qualifier.WsV4Qualifier;
import com.tokopedia.seller.shop.setting.data.datasource.cloud.OpenShopApi;
import com.tokopedia.core.network.di.qualifier.WsV4QualifierWithErrorHander;
import com.tokopedia.seller.product.edit.data.source.cloud.api.GenerateHostApi;
import com.tokopedia.seller.shop.setting.data.repository.DistrictLogisticDataRepositoryImpl;
import com.tokopedia.seller.shop.setting.data.repository.ShopSettingSaveInfoRepositoryImpl;
import com.tokopedia.seller.shop.setting.data.source.DistrictDataSource;
import com.tokopedia.seller.shop.setting.data.source.LogisticDataSource;
import com.tokopedia.seller.shop.setting.data.source.ShopSettingInfoDataSource;
import com.tokopedia.seller.shop.setting.di.scope.ShopSettingInfoScope;
import com.tokopedia.seller.shop.setting.di.scope.ShopSettingScope;
import com.tokopedia.seller.shop.setting.domain.DistrictLogisticDataRepository;
import com.tokopedia.seller.shop.setting.domain.ShopSettingSaveInfoRepository;

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