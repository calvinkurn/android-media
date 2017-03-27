package com.tokopedia.seller.shop.setting.di.module;

import com.tokopedia.core.network.apiservices.shop.MyShopService;
import com.tokopedia.core.network.apiservices.shop.apis.MyShopApi;
import com.tokopedia.seller.shop.setting.data.repository.DistrictLogisticDataRepositoryImpl;
import com.tokopedia.seller.shop.setting.data.repository.ShopSettingSaveInfoRepositoryImpl;
import com.tokopedia.seller.shop.setting.data.source.DistrictDataSource;
import com.tokopedia.seller.shop.setting.data.source.LogisticDataSource;
import com.tokopedia.seller.shop.setting.data.source.ShopSettingInfoDataSource;
import com.tokopedia.seller.shop.setting.data.source.cloud.apiservice.GenerateHostApi;
import com.tokopedia.seller.shop.setting.data.source.cloud.apiservice.GenerateHostService;
import com.tokopedia.seller.shop.setting.di.scope.ShopSettingScope;
import com.tokopedia.seller.shop.setting.domain.DistrictLogisticDataRepository;
import com.tokopedia.seller.shop.setting.domain.ShopSettingSaveInfoRepository;

import dagger.Module;
import dagger.Provides;

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
    public MyShopApi provideMyShopApi() {
        return new MyShopService().getApi();
    }

    @Provides
    @ShopSettingScope
    public ShopSettingSaveInfoRepository provideSaveInfoRepository(ShopSettingInfoDataSource shopSettingInfoDataSource) {
        return new ShopSettingSaveInfoRepositoryImpl(shopSettingInfoDataSource);
    }

    @Provides
    @ShopSettingScope
    public GenerateHostApi provideGenerateHostApi() {
        return new GenerateHostService().getApi();
    }
}