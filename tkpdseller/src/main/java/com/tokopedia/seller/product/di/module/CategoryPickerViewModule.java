package com.tokopedia.seller.product.di.module;

import com.tokopedia.seller.product.di.scope.CategoryPickerViewScope;
import com.tokopedia.seller.product.domain.interactor.FetchAllCategoryDataUseCase;
import com.tokopedia.seller.product.domain.interactor.FetchCategoryChildUseCase;
import com.tokopedia.seller.product.domain.interactor.FetchCategoryFromSelectedUseCase;
import com.tokopedia.seller.product.view.presenter.CategoryPickerPresenterImpl;
import com.tokopedia.seller.product.view.presenter.CategoryPickerPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * @author sebastianuskh on 4/3/17.
 */
@Module
@CategoryPickerViewScope
public class CategoryPickerViewModule {

    @CategoryPickerViewScope
    @Provides
    CategoryPickerPresenter provideCategoryPickerPresenter(
            FetchCategoryChildUseCase fetchCategoryChildUseCase,
            FetchAllCategoryDataUseCase fetchAllCategoryDataUseCase,
            FetchCategoryFromSelectedUseCase fetchCategoryFromSelectedUseCase
    ){
        return new CategoryPickerPresenterImpl(fetchCategoryChildUseCase, fetchAllCategoryDataUseCase, fetchCategoryFromSelectedUseCase);
    }

}
