package com.tokopedia.seller.product.di.module;

import com.tokopedia.seller.product.di.scope.CategoryPickerScope;
import com.tokopedia.seller.product.di.scope.CategoryPickerViewScope;
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
    CategoryPickerPresenter provideCategoryPickerPresenter(){
        return new CategoryPickerPresenterImpl();
    }

}
