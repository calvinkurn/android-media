package com.tokopedia.seller.shop.setting.di.module;

import android.app.Activity;

import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.network.apiservices.shop.MyShopService;
import com.tokopedia.core.network.apiservices.shop.apis.MyShopApi;
import com.tokopedia.seller.app.BaseActivityModule;
import com.tokopedia.seller.shop.setting.data.repository.DistrictDataRepositoryImpl;
import com.tokopedia.seller.shop.setting.data.source.DistrictDataSource;
import com.tokopedia.seller.shop.setting.data.source.cache.DistrictDataCache;
import com.tokopedia.seller.shop.setting.data.source.cloud.DistrictDataCloud;
import com.tokopedia.seller.shop.setting.di.scope.ShopSettingScope;
import com.tokopedia.seller.shop.setting.domain.DistrictDataRepository;
import com.tokopedia.seller.shop.setting.domain.interactor.FetchDistrictDataUseCase;

import dagger.Module;
import dagger.Provides;

/**
 * Created by sebastianuskh on 3/20/17.
 */
@ShopSettingScope
@Module
public class ShopSettingModule extends BaseActivityModule {
    public ShopSettingModule(Activity activity) {
        super(activity);
    }

    @Provides
    @ShopSettingScope
    public FetchDistrictDataUseCase provideFetchDistrictDataUseCase(
            ThreadExecutor threadExecutor,
            PostExecutionThread postExecutionThread,
            DistrictDataRepository districtDataRepository
    ) {
        return new FetchDistrictDataUseCase(threadExecutor, postExecutionThread, districtDataRepository);
    }

    @Provides
    @ShopSettingScope
    public DistrictDataRepository provideDistrictDataRepository(DistrictDataSource districtDataSource) {
        return new DistrictDataRepositoryImpl(districtDataSource);
    }

    @Provides
    @ShopSettingScope
    public DistrictDataCache provideDistrictDataCache() {
        return new DistrictDataCache();
    }

    @Provides
    @ShopSettingScope
    public DistrictDataCloud provideDistrictDataCloud(MyShopApi api, Activity activity) {
        return new DistrictDataCloud(api, activity);
    }

    @Provides
    @ShopSettingScope
    public MyShopApi provideMyShopApi() {
        return new MyShopService().getApi();
    }
}