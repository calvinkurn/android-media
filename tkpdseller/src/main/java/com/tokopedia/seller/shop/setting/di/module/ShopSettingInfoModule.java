package com.tokopedia.seller.shop.setting.di.module;

import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.seller.app.BaseFragmentModule;
import com.tokopedia.seller.shop.setting.di.scope.ShopSettingInfoScope;
import com.tokopedia.seller.shop.setting.domain.ShopSettingSaveInfoRepository;
import com.tokopedia.seller.shop.setting.domain.interactor.ShopSettingSaveInfoUseCase;
import com.tokopedia.seller.shop.setting.view.presenter.ShopSettingInfoPresenter;
import com.tokopedia.seller.shop.setting.view.presenter.ShopSettingInfoPresenterImpl;
import com.tokopedia.seller.shop.setting.view.presenter.ShopSettingInfoView;

import dagger.Module;
import dagger.Provides;

/**
 * Created by zulfikarrahman on 3/23/17.
 */

@ShopSettingInfoScope
@Module
public class ShopSettingInfoModule extends BaseFragmentModule<ShopSettingInfoView> {
    public ShopSettingInfoModule(ShopSettingInfoView view) {
        super(view);
    }

    @Provides
    ShopSettingInfoPresenter providePresenter(ShopSettingInfoView shopSettingInfoView,
                                              ShopSettingSaveInfoUseCase shopSettingSaveInfoUseCase){
        return new ShopSettingInfoPresenterImpl(shopSettingInfoView, shopSettingSaveInfoUseCase);
    }

    @Provides
    ShopSettingSaveInfoUseCase provideSaveInfoUseCase(ThreadExecutor threadExecutor,
                                                      PostExecutionThread postExecutionThread,
                                                      ShopSettingSaveInfoRepository saveInfoRepository){
        return new ShopSettingSaveInfoUseCase(threadExecutor, postExecutionThread, saveInfoRepository);
    }
}
