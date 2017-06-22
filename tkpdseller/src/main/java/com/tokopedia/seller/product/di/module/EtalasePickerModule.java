package com.tokopedia.seller.product.di.module;

import com.tokopedia.core.network.di.qualifier.WsV4QualifierWithErrorHander;
import com.tokopedia.seller.product.data.repository.MyEtalaseRepositoryImpl;
import com.tokopedia.seller.product.data.source.MyEtalaseDataSource;
import com.tokopedia.seller.product.data.source.cloud.api.MyEtalaseApi;
import com.tokopedia.seller.product.di.scope.EtalasePickerScope;
import com.tokopedia.seller.product.domain.MyEtalaseRepository;
import com.tokopedia.seller.product.domain.interactor.AddNewEtalaseUseCase;
import com.tokopedia.seller.product.domain.interactor.FetchMyEtalaseUseCase;
import com.tokopedia.seller.product.view.presenter.EtalasePickerPresenter;
import com.tokopedia.seller.product.view.presenter.EtalasePickerPresenterImpl;
import com.tokopedia.seller.topads.dashboard.data.factory.TopAdsEtalaseFactory;
import com.tokopedia.seller.topads.dashboard.data.repository.TopAdsEtalaseListRepositoryImpl;
import com.tokopedia.seller.topads.domain.TopAdsEtalaseListRepository;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * @author sebastianuskh on 4/5/17.
 */
@EtalasePickerScope
@Module
public class EtalasePickerModule {
    @EtalasePickerScope
    @Provides
    TopAdsEtalaseListRepository provideTopAdsEtalaseListRepository(TopAdsEtalaseFactory topadsEtalaseFactory){
        return new TopAdsEtalaseListRepositoryImpl(topadsEtalaseFactory);
    }

    @EtalasePickerScope
    @Provides
    MyEtalaseRepository provideMyEtalaseRepository(MyEtalaseDataSource myEtalaseDataSource){
        return new MyEtalaseRepositoryImpl(myEtalaseDataSource);
    }

    @EtalasePickerScope
    @Provides
    MyEtalaseApi provideMyEtalaseApi(@WsV4QualifierWithErrorHander Retrofit retrofit){
        return retrofit.create(MyEtalaseApi.class);
    }

    @EtalasePickerScope
    @Provides
    EtalasePickerPresenter provideEtalasePickerPresenter(FetchMyEtalaseUseCase fetchMyEtalaseUseCase, AddNewEtalaseUseCase addNewEtalaseUseCase){
        return new EtalasePickerPresenterImpl(fetchMyEtalaseUseCase, addNewEtalaseUseCase);
    }

}
