package com.tokopedia.seller.shop.open.di.module;

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
import com.tokopedia.seller.shop.open.domain.ShopOpenSaveInfoRepository;
import com.tokopedia.seller.shop.open.domain.interactor.ShopIsReserveDomainUseCase;
import com.tokopedia.seller.shop.open.di.scope.ShopOpenDomainScope;
import com.tokopedia.seller.shop.open.data.model.UploadShopImageModel;
import com.tokopedia.seller.shop.open.data.repository.ShopOpenSaveInfoRepositoryImpl;
import com.tokopedia.seller.shop.open.data.source.ShopOpenInfoDataSource;
import com.tokopedia.seller.shop.open.domain.interactor.ShopOpenSaveInfoUseCase;
import com.tokopedia.seller.shop.open.view.presenter.ShopOpenInfoPresenter;
import com.tokopedia.seller.shop.open.view.presenter.ShopOpenInfoPresenterImpl;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * Created by zulfikarrahman on 3/23/17.
 */

@ShopOpenDomainScope
@Module
public class ShopSettingInfoModule {

    @Provides
    @ShopOpenDomainScope
    ShopOpenInfoPresenter providePresenter(ShopOpenSaveInfoUseCase shopOpenSaveInfoUseCase, ShopIsReserveDomainUseCase shopIsReserveDomainUseCase) {
        return new ShopOpenInfoPresenterImpl(shopOpenSaveInfoUseCase, shopIsReserveDomainUseCase);
    }

    @Provides
    @ShopOpenDomainScope
    GenerateHostRepository provideGenerateHostRepository(GenerateHostDataSource generateHostDataSource){
        return new GenerateHostRepositoryImpl(generateHostDataSource);
    }

    @Provides
    @ShopOpenDomainScope
    UploadImageRepository provideUploadImageRepository(UploadImageDataSource uploadImageDataSource){
        return new UploadImageRepositoryImpl(uploadImageDataSource);
    }

    @Provides
    @ShopOpenDomainScope
    ShopOpenSaveInfoRepository provideShopSettingSaveInfoRepository(ShopOpenInfoDataSource shopOpenInfoDataSource){
        return new ShopOpenSaveInfoRepositoryImpl(shopOpenInfoDataSource);
    }

    @Provides
    @ShopOpenDomainScope
    GenerateHostApi provideGenerateHostApi(@WsV4QualifierWithErrorHander Retrofit retrofit){
        return retrofit.create(GenerateHostApi.class);
    }

    @Provides
    @ShopOpenDomainScope
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
    @ShopOpenDomainScope
    NetworkCalculator provideNetworkCalculator(@ApplicationContext Context context){
        return new NetworkCalculator(NetworkConfig.POST, context, TkpdBaseURL.DEFAULT_TOKOPEDIA_WEBSITE_URL).setIdentity().compileAllParam().finish();
    }
}
