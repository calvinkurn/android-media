package com.tokopedia.seller.shop.setting.di.module;

import android.content.Context;

import com.tokopedia.core.base.di.qualifier.ActivityContext;
import com.tokopedia.core.base.di.scope.ActivityScope;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.seller.app.BaseFragmentModule;
import com.tokopedia.seller.shop.setting.data.repository.ShopOpenRepositoryImpl;
import com.tokopedia.seller.shop.setting.data.source.ShopOpenDataSource;
import com.tokopedia.seller.shop.setting.data.source.cloud.api.TomeApi;
import com.tokopedia.seller.shop.setting.data.source.cloud.service.TomeService;
import com.tokopedia.seller.shop.setting.domain.ShopOpenRepository;
import com.tokopedia.seller.shop.setting.domain.interactor.CheckDomainNameUseCase;
import com.tokopedia.seller.shop.setting.domain.interactor.CheckShopNameUseCase;
import com.tokopedia.seller.shop.setting.view.presenter.ShopOpenDomainPresenter;
import com.tokopedia.seller.shop.setting.view.presenter.ShopOpenDomainPresenterImpl;
import com.tokopedia.seller.shop.setting.view.presenter.ShopOpenDomainView;

import dagger.Module;
import dagger.Provides;

/**
 * Created by sebastianuskh on 3/20/17.
 */
@ActivityScope
@Module
public class ShopOpenDomainModule {

    @Provides
    ShopOpenDomainPresenter providePresenter(CheckDomainNameUseCase checkDomainNameUseCase,
                                             CheckShopNameUseCase checkShopNameUseCase) {
        return new ShopOpenDomainPresenterImpl(
                checkDomainNameUseCase, checkShopNameUseCase) {
        };
    }

    // Provide CheckDomainNameUseCase
    // Provide CheckShopNameUseCase

    @Provides
    public ShopOpenRepository provideShopOpenRepository(ShopOpenDataSource shopOpenDataSource) {
        return new ShopOpenRepositoryImpl(shopOpenDataSource);
    }

    @Provides
    public TomeService provideTomeService(@ActivityContext Context context) {
        return new TomeService(SessionHandler.getAccessToken(context));
    }

    @Provides
    public TomeApi provideTomeApi(TomeService tomeService) {
        return tomeService.getApi();
    }
}