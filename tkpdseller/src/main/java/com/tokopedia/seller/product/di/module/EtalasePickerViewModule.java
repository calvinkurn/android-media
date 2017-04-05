package com.tokopedia.seller.product.di.module;

import com.tokopedia.seller.product.di.scope.EtalasePickerViewScope;
import com.tokopedia.seller.product.view.presenter.EtalasePickerPresenterImpl;
import com.tokopedia.seller.product.view.presenter.EtalasePickerPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * @author sebastianuskh on 4/5/17.
 */
@EtalasePickerViewScope
@Module
public class EtalasePickerViewModule {

    @EtalasePickerViewScope
    @Provides
    EtalasePickerPresenter provideEtalasePickerPresenter(){
        return new EtalasePickerPresenterImpl();
    }
}
