package com.tokopedia.flight.common.di.module;

import android.content.Context;

import com.tokopedia.flight.common.di.scope.FlightScope;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * Created by User on 10/24/2017.
 */

@FlightScope
@Module
public class FlightModule {

//    @FlightScope
//    @Provides
//    DatePickerPresenter provideDatePickerPresenter(FetchDatePickerUseCase fetchDatePickerUseCase,
//                                                   SaveDatePickerUseCase saveDatePickerUseCase,
//                                                   ClearDatePickerUseCase clearDatePickerUseCase) {
//        return new DatePickerPresenterImpl(fetchDatePickerUseCase, saveDatePickerUseCase, clearDatePickerUseCase);
//    }
//
//    @ProductScope
//    @Provides
//    DatePickerRepository provideDatePickerRepository(DatePickerDataSource datePickerDataSource) {
//        return new DatePickerRepositoryImpl(datePickerDataSource);
//    }
//
//    @ProductScope
//    @Provides
//    GlobalCacheManager provideGlobalCacheManager(){
//        return new GlobalCacheManager();
//    }
//
//    @ProductScope
//    @Provides
//    public ShopInfoRepository provideShopInfoRepository(@ApplicationContext Context context, ShopInfoDataSource shopInfoDataSource){
//        return new ShopInfoRepositoryImpl(context, shopInfoDataSource);
//    }
//
//    @ProductScope
//    @Provides
//    public ShopApi provideShopApi(@WsV4QualifierWithErrorHander Retrofit retrofit){
//        return retrofit.create(ShopApi.class);
//    }
}