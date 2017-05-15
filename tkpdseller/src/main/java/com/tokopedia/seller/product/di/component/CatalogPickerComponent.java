package com.tokopedia.seller.product.di.component;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.di.scope.ActivityScope;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.seller.product.di.module.CatalogPickerModule;
import com.tokopedia.seller.product.di.module.CategoryPickerModule;
import com.tokopedia.seller.product.di.scope.CategoryPickerScope;
import com.tokopedia.seller.product.domain.CatalogRepository;
import com.tokopedia.seller.product.domain.CategoryRepository;
import com.tokopedia.seller.product.view.presenter.CatalogPickerPresenter;

import dagger.Component;

/**
 * @author sebastianuskh on 4/3/17.
 */
@ActivityScope
@Component (modules = CatalogPickerModule.class, dependencies = AppComponent.class)
public interface CatalogPickerComponent {

    CatalogPickerPresenter catalogPickerPresenter();

    ThreadExecutor threadExecutor();

    PostExecutionThread postExecutionThread();
}
