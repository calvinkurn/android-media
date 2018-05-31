package com.tokopedia.seller.shop.open.di.module;

import android.content.Context;

import com.google.gson.Gson;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.imageuploader.di.ImageUploaderModule;
import com.tokopedia.imageuploader.di.qualifier.ImageUploaderQualifier;
import com.tokopedia.imageuploader.domain.GenerateHostRepository;
import com.tokopedia.imageuploader.domain.UploadImageRepository;
import com.tokopedia.imageuploader.domain.UploadImageUseCase;
import com.tokopedia.imageuploader.utils.ImageUploaderUtils;
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
@Module(includes = ImageUploaderModule.class)
public class ShopSettingInfoModule {

    @Provides
    @ShopOpenDomainScope
    ShopOpenInfoPresenter providePresenter(ShopOpenSaveInfoUseCase shopOpenSaveInfoUseCase, ShopIsReserveDomainUseCase shopIsReserveDomainUseCase) {
        return new ShopOpenInfoPresenterImpl(shopOpenSaveInfoUseCase, shopIsReserveDomainUseCase);
    }


    @Provides
    @ShopOpenDomainScope
    ShopOpenSaveInfoRepository provideShopSettingSaveInfoRepository(ShopOpenInfoDataSource shopOpenInfoDataSource){
        return new ShopOpenSaveInfoRepositoryImpl(shopOpenInfoDataSource);
    }

    @Provides
    @ShopOpenDomainScope
    UploadImageUseCase<UploadShopImageModel> provideUploadImageUseCase(@ImageUploaderQualifier UploadImageRepository uploadImageRepository,
                                                                       @ImageUploaderQualifier GenerateHostRepository generateHostRepository,
                                                                       @ImageUploaderQualifier Gson gson,
                                                                       @ImageUploaderQualifier UserSession userSession,
                                                                       @ImageUploaderQualifier ImageUploaderUtils imageUploaderUtils){
        return new UploadImageUseCase<>(uploadImageRepository, generateHostRepository, gson, userSession, UploadShopImageModel.class, imageUploaderUtils);
    }

    @ApplicationContext
    @Provides
    @ShopOpenDomainScope
    Context context(@com.tokopedia.core.base.di.qualifier.ApplicationContext Context context){
        return context;
    }
}
