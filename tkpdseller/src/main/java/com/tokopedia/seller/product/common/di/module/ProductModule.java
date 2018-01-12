package com.tokopedia.seller.product.common.di.module;

import android.content.Context;

import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.network.di.qualifier.WsV4QualifierWithErrorHander;
import com.tokopedia.seller.base.data.repository.DatePickerRepositoryImpl;
import com.tokopedia.seller.base.data.source.DatePickerDataSource;
import com.tokopedia.seller.base.domain.DatePickerRepository;
import com.tokopedia.seller.base.domain.interactor.ClearDatePickerUseCase;
import com.tokopedia.seller.base.domain.interactor.FetchDatePickerUseCase;
import com.tokopedia.seller.base.domain.interactor.SaveDatePickerUseCase;
import com.tokopedia.seller.base.view.presenter.DatePickerPresenter;
import com.tokopedia.seller.base.view.presenter.DatePickerPresenterImpl;
import com.tokopedia.seller.product.common.di.scope.ProductScope;
import com.tokopedia.seller.shop.common.domain.repository.ShopInfoRepositoryImpl;
import com.tokopedia.seller.shop.common.data.source.ShopInfoDataSource;
import com.tokopedia.seller.shop.common.data.source.cloud.api.ShopApi;
import com.tokopedia.seller.shop.common.domain.repository.ShopInfoRepository;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * @author sebastianuskh on 4/13/17.
 */
@ProductScope
@Module
public class ProductModule {

    @ProductScope
    @Provides
    DatePickerPresenter provideDatePickerPresenter(FetchDatePickerUseCase fetchDatePickerUseCase,
                                                   SaveDatePickerUseCase saveDatePickerUseCase,
                                                   ClearDatePickerUseCase clearDatePickerUseCase) {
        return new DatePickerPresenterImpl(fetchDatePickerUseCase, saveDatePickerUseCase, clearDatePickerUseCase);
    }

    @ProductScope
    @Provides
    DatePickerRepository provideDatePickerRepository(DatePickerDataSource datePickerDataSource) {
        return new DatePickerRepositoryImpl(datePickerDataSource);
    }

    @ProductScope
    @Provides
    GlobalCacheManager provideGlobalCacheManager(){
        return new GlobalCacheManager();
    }

    @ProductScope
    @Provides
    public ShopInfoRepository provideShopInfoRepository(@ApplicationContext Context context, ShopInfoDataSource shopInfoDataSource){
        return new ShopInfoRepositoryImpl(context, shopInfoDataSource);
    }

    @ProductScope
    @Provides
    public ShopApi provideShopApi(@WsV4QualifierWithErrorHander Retrofit retrofit){
        return retrofit.create(ShopApi.class);
    }
}