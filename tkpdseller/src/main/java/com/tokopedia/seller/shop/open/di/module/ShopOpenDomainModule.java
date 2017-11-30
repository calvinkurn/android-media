package com.tokopedia.seller.shop.open.di.module;

import android.content.Context;

import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.core.network.di.qualifier.TomeQualifier;
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
import retrofit2.Retrofit;

@ShopOpenDomainScope
@Module
public class ShopOpenDomainModule {

    @ShopOpenDomainScope
    @Provides
    ShopOpenDomainPresenter providePresenter(CheckDomainNameUseCase checkDomainNameUseCase,
                                             CheckShopNameUseCase checkShopNameUseCase) {
        return new ShopOpenDomainPresenterImpl(
                checkDomainNameUseCase, checkShopNameUseCase) {
        };
    }

    @ShopOpenDomainScope
    @Provides
    public ShopOpenRepository provideShopOpenRepository(ShopOpenDataSource shopOpenDataSource) {
        return new ShopOpenRepositoryImpl(shopOpenDataSource);
    }

    @ShopOpenDomainScope
    @Provides
    public TomeApi provideTomeApi(@TomeQualifier Retrofit retrofit) {
        return retrofit.create(TomeApi.class);
    }
}