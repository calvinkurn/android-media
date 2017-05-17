package com.tokopedia.seller.product.di.module;

import com.tokopedia.core.base.di.scope.ActivityScope;
import com.tokopedia.core.network.di.qualifier.AceQualifier;
import com.tokopedia.seller.product.data.repository.CatalogRepositoryImpl;
import com.tokopedia.seller.product.data.source.CatalogDataSource;
import com.tokopedia.seller.product.data.source.cloud.api.SearchApi;
import com.tokopedia.seller.product.domain.CatalogRepository;
import com.tokopedia.seller.product.domain.interactor.FetchCatalogDataUseCase;
import com.tokopedia.seller.product.view.presenter.CatalogPickerPresenter;
import com.tokopedia.seller.product.view.presenter.CatalogPickerPresenterImpl;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * @author sebastianuskh on 4/3/17.
 */
@ActivityScope
@Module
public class CatalogPickerModule {

    @ActivityScope
    @Provides
    CatalogPickerPresenter provideCatalogPickerPresenter(FetchCatalogDataUseCase fetchCatalogDataUseCase){
        return new CatalogPickerPresenterImpl(fetchCatalogDataUseCase);
    }

    @ActivityScope
    @Provides
    CatalogRepository provideCatalogRepository(CatalogDataSource catalogDataSource){
        return new CatalogRepositoryImpl(catalogDataSource);
    }

    @ActivityScope
    @Provides
    SearchApi provideSearchApi(@AceQualifier Retrofit retrofit){
        return retrofit.create(SearchApi.class);
    }

}
