package com.tokopedia.discovery.newdiscovery.category.di.component;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.discovery.newdiscovery.category.di.module.CategoryModule;
import com.tokopedia.discovery.newdiscovery.category.di.scope.CategoryScope;
import com.tokopedia.discovery.newdiscovery.category.presentation.CategoryActivity;
import com.tokopedia.discovery.newdiscovery.category.presentation.CategoryPresenter;
import com.tokopedia.discovery.newdiscovery.category.presentation.product.ProductFragment;
import com.tokopedia.discovery.newdiscovery.category.presentation.product.ProductPresenter;
import com.tokopedia.discovery.newdiscovery.search.fragment.catalog.CatalogFragment;
import com.tokopedia.discovery.newdiscovery.search.fragment.catalog.presenter.CatalogPresenter;

import dagger.Component;

/**
 * @author by alifa on 10/26/17.
 */

@CategoryScope
@Component (modules = CategoryModule.class, dependencies = AppComponent.class)
public interface CategoryComponent {
    void inject (CategoryActivity categoryActivity);

    void inject (CategoryPresenter categoryPresenter);

    void inject(ProductFragment productFragment);

    void inject(ProductPresenter presenter);

    void inject(CatalogFragment catalogFragment);

    void inject(CatalogPresenter presenter);

}
