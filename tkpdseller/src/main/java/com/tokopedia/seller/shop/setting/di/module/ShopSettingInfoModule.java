package com.tokopedia.seller.shop.setting.di.module;

import android.content.Context;

import com.google.gson.Gson;
import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.di.qualifier.WsV4QualifierWithErrorHander;
import com.tokopedia.core.network.retrofit.utils.NetworkCalculator;
import com.tokopedia.core.network.v4.NetworkConfig;
import com.tokopedia.seller.base.data.repository.UploadImageRepositoryImpl;
import com.tokopedia.seller.base.data.source.UploadImageDataSource;
import com.tokopedia.seller.base.domain.UploadImageRepository;
import com.tokopedia.seller.base.domain.interactor.UploadImageUseCase;
import com.tokopedia.seller.product.edit.data.repository.GenerateHostRepositoryImpl;
import com.tokopedia.seller.product.edit.data.source.GenerateHostDataSource;
import com.tokopedia.seller.product.edit.data.source.cloud.api.GenerateHostApi;
import com.tokopedia.seller.product.edit.domain.GenerateHostRepository;
import com.tokopedia.seller.shop.setting.data.model.UploadShopImageModel;
import com.tokopedia.seller.shop.setting.data.repository.ShopSettingSaveInfoRepositoryImpl;
import com.tokopedia.seller.shop.setting.data.source.ShopSettingInfoDataSource;
import com.tokopedia.seller.shop.setting.di.scope.ShopSettingInfoScope;
import com.tokopedia.seller.shop.setting.domain.ShopSettingSaveInfoRepository;
import com.tokopedia.seller.shop.setting.domain.interactor.ShopSettingSaveInfoUseCase;
import com.tokopedia.seller.shop.setting.view.presenter.ShopSettingInfoPresenter;
import com.tokopedia.seller.shop.setting.view.presenter.ShopSettingInfoPresenterImpl;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * Created by zulfikarrahman on 3/23/17.
 */

@ShopSettingInfoScope
@Module
public class ShopSettingInfoModule {

    @Provides
    @ShopSettingInfoScope
    ShopSettingInfoPresenter providePresenter(ShopSettingSaveInfoUseCase shopSettingSaveInfoUseCase) {
        return new ShopSettingInfoPresenterImpl(shopSettingSaveInfoUseCase);
    }

    @Provides
    @ShopSettingInfoScope
    GenerateHostRepository provideGenerateHostRepository(GenerateHostDataSource generateHostDataSource){
        return new GenerateHostRepositoryImpl(generateHostDataSource);
    }

    @Provides
    @ShopSettingInfoScope
    Gson provideGson(){
        return new Gson();
    }

    @Provides
    @ShopSettingInfoScope
    UploadImageRepository provideUploadImageRepository(UploadImageDataSource uploadImageDataSource){
        return new UploadImageRepositoryImpl(uploadImageDataSource);
    }

    @Provides
    @ShopSettingInfoScope
    ShopSettingSaveInfoRepository provideShopSettingSaveInfoRepository(ShopSettingInfoDataSource shopSettingInfoDataSource){
        return new ShopSettingSaveInfoRepositoryImpl(shopSettingInfoDataSource);
    }

    @Provides
    @ShopSettingInfoScope
    GenerateHostApi provideGenerateHostApi(@WsV4QualifierWithErrorHander Retrofit retrofit){
        return retrofit.create(GenerateHostApi.class);
    }

    @Provides
    @ShopSettingInfoScope
    UploadImageUseCase<UploadShopImageModel> provideUploadImageUseCase(ThreadExecutor threadExecutor,
                                                                       PostExecutionThread postExecutionThread,
                                                                       UploadImageRepository uploadImageRepository,
                                                                       GenerateHostRepository generateHostRepository,
                                                                       Gson gson,
                                                                       NetworkCalculator networkCalculator){
        return new UploadImageUseCase<UploadShopImageModel>(threadExecutor, postExecutionThread, uploadImageRepository,
                generateHostRepository, gson, networkCalculator, UploadShopImageModel.class);
    }

    @Provides
    @ShopSettingInfoScope
    NetworkCalculator provideNetworkCalculator(@ApplicationContext Context context){
        return new NetworkCalculator(NetworkConfig.POST, context, TkpdBaseURL.DEFAULT_TOKOPEDIA_WEBSITE_URL);
    }
}
