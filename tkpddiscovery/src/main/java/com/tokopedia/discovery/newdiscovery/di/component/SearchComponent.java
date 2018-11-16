package com.tokopedia.discovery.newdiscovery.di.component;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.discovery.imagesearch.search.ImageSearchActivity;
import com.tokopedia.discovery.imagesearch.search.ImageSearchPresenter;
import com.tokopedia.discovery.imagesearch.search.fragment.ImageSearchProductListFragment;
import com.tokopedia.discovery.imagesearch.search.fragment.product.ImageProductListPresenterImpl;
import com.tokopedia.discovery.newdiscovery.category.presentation.CategoryCatalogFragment;
import com.tokopedia.discovery.newdiscovery.di.module.SearchModule;
import com.tokopedia.discovery.newdiscovery.di.scope.SearchScope;
import com.tokopedia.discovery.newdiscovery.search.SearchActivity;
import com.tokopedia.discovery.newdiscovery.search.fragment.catalog.CatalogFragment;
import com.tokopedia.discovery.newdiscovery.search.fragment.catalog.presenter.CatalogPresenter;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.ProductListFragment;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.ProductListPresenterImpl;
import com.tokopedia.discovery.newdiscovery.search.fragment.shop.ShopListFragment;
import com.tokopedia.discovery.newdiscovery.search.fragment.shop.ShopListPresenterImpl;

import dagger.Component;

/**
 * Created by henrypriyono on 10/10/17.
 */

@SearchScope
@Component(modules = SearchModule.class, dependencies = AppComponent.class)
public interface SearchComponent {
    void inject(SearchActivity searchActivity);

    void inject(ImageSearchActivity imageSearchActiviy);

    void inject(ProductListFragment productListFragment);

    void inject(ImageSearchProductListFragment imageProductListFragment);

    void inject(ProductListPresenterImpl presenter);

    void inject(ImageProductListPresenterImpl imageProductListPresenter);

    void inject(CatalogFragment catalogFragment);

    void inject(CategoryCatalogFragment catalogFragment);

    void inject(CatalogPresenter presenter);

    void inject(ShopListFragment shopListFragment);

    void inject(ShopListPresenterImpl presenter);

    void inject(ImageSearchPresenter imageSearchPresenter);
}
