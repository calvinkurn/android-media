package com.tokopedia.seller.product.edit.di.module;

import com.tokopedia.core.network.di.qualifier.HadesQualifier;
import com.tokopedia.seller.product.category.data.repository.CategoryRepositoryImpl;
import com.tokopedia.seller.product.category.data.source.CategoryDataSource;
import com.tokopedia.seller.product.category.data.source.CategoryVersionDataSource;
import com.tokopedia.seller.product.category.data.source.FetchCategoryDataSource;
import com.tokopedia.seller.product.edit.data.source.cloud.api.HadesCategoryApi;
import com.tokopedia.seller.product.edit.di.scope.CategoryPickerScope;
import com.tokopedia.seller.product.category.domain.CategoryRepository;
import com.tokopedia.seller.product.category.domain.interactor.FetchCategoryFromSelectedUseCase;
import com.tokopedia.seller.product.category.domain.interactor.FetchCategoryWithParentChildUseCase;
import com.tokopedia.seller.product.category.view.presenter.CategoryPickerPresenter;
import com.tokopedia.seller.product.category.view.presenter.CategoryPickerPresenterImpl;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * @author sebastianuskh on 4/3/17.
 */
@CategoryPickerScope
@Module
public class CategoryPickerModule {

    @CategoryPickerScope
    @Provides
    CategoryRepository provideCategoryRepository(CategoryVersionDataSource categoryVersionDataSource,
                                                 CategoryDataSource categoryDataSource,
                                                 FetchCategoryDataSource fetchCategoryDataSource){
        return new CategoryRepositoryImpl(categoryVersionDataSource, categoryDataSource, fetchCategoryDataSource);
    }

    @CategoryPickerScope
    @Provides
    HadesCategoryApi provideHadesCategoryApi(@HadesQualifier Retrofit retrofit){
        return retrofit.create(HadesCategoryApi.class);
    }


    @CategoryPickerScope
    @Provides
    CategoryPickerPresenter provideCategoryPickerPresenter(
            FetchCategoryWithParentChildUseCase fetchCategoryChildUseCase,
            FetchCategoryFromSelectedUseCase fetchCategoryFromSelectedUseCase
    ){
        return new CategoryPickerPresenterImpl(fetchCategoryChildUseCase, fetchCategoryFromSelectedUseCase);
    }

}
