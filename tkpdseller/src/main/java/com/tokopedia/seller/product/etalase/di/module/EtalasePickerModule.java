package com.tokopedia.seller.product.etalase.di.module;

import com.tokopedia.core.network.di.qualifier.WsV4QualifierWithErrorHander;
import com.tokopedia.seller.product.etalase.data.repository.MyEtalaseRepositoryImpl;
import com.tokopedia.seller.product.etalase.data.source.MyEtalaseDataSource;
import com.tokopedia.seller.product.etalase.data.source.cloud.api.MyEtalaseApi;
import com.tokopedia.seller.product.etalase.di.scope.EtalasePickerScope;
import com.tokopedia.seller.product.etalase.domain.MyEtalaseRepository;
import com.tokopedia.seller.product.etalase.domain.interactor.AddNewEtalaseUseCase;
import com.tokopedia.seller.product.etalase.domain.interactor.FetchMyEtalaseUseCase;
import com.tokopedia.seller.product.etalase.view.presenter.EtalasePickerPresenter;
import com.tokopedia.seller.product.etalase.view.presenter.EtalasePickerPresenterImpl;

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
