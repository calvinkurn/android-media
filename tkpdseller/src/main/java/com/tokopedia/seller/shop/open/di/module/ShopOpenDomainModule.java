package com.tokopedia.seller.shop.open.di.module;

import com.tokopedia.core.network.di.qualifier.TomeQualifier;
import com.tokopedia.seller.shop.open.data.repository.ShopOpenRepository;
import com.tokopedia.seller.shop.open.data.repository.ShopOpenRepositoryImpl;
import com.tokopedia.seller.shop.open.data.source.ShopOpenDataSource;
import com.tokopedia.seller.shop.open.data.source.cloud.api.TomeApi;
import com.tokopedia.seller.shop.open.di.scope.ShopOpenDomainScope;
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
    public ShopOpenRepository provideShopOpenRepository(ShopOpenDataSource shopOpenDataSource) {
        return new ShopOpenRepositoryImpl(shopOpenDataSource);
    }

    @ShopOpenDomainScope
    @Provides
    public TomeApi provideTomeApi(@TomeQualifier Retrofit retrofit) {
        return retrofit.create(TomeApi.class);
    }
}