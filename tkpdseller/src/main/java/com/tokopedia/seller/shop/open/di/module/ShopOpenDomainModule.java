package com.tokopedia.seller.shop.open.di.module;

import android.content.Context;

import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.seller.shop.open.data.repository.ShopOpenRepositoryImpl;
import com.tokopedia.seller.shop.open.data.source.ShopOpenDataSource;
import com.tokopedia.seller.shop.open.data.source.cloud.api.TomeApi;
import com.tokopedia.seller.shop.open.di.scope.ShopOpenDomainScope;
import com.tokopedia.seller.shop.setting.data.source.cloud.service.TomeService;
import com.tokopedia.seller.shop.open.domain.ShopOpenRepository;
import com.tokopedia.seller.shop.open.domain.interactor.CheckDomainNameUseCase;
import com.tokopedia.seller.shop.open.domain.interactor.CheckShopNameUseCase;
import com.tokopedia.seller.shop.open.view.presenter.ShopOpenDomainPresenter;
import com.tokopedia.seller.shop.open.view.presenter.ShopOpenDomainPresenterImpl;

import dagger.Module;
import dagger.Provides;

/**
 * Created by sebastianuskh on 3/20/17.
 */
@ShopOpenDomainScope
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
    public TomeService provideTomeService(@ApplicationContext Context context) {
        return new TomeService(SessionHandler.getAccessToken(context));
    }

    @Provides
    public TomeApi provideTomeApi(TomeService tomeService) {
        return tomeService.getApi();
    }
}