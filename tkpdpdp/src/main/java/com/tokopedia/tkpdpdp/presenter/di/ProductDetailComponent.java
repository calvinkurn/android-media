package com.tokopedia.tkpdpdp.presenter.di;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.tkpdpdp.fragment.ProductDetailFragment;
import com.tokopedia.tkpdpdp.presenter.di.scope.ProductDetailScope;

import dagger.Component;

@ProductDetailScope
@Component(modules = {ProductDetailModule.class}, dependencies = AppComponent.class)
public interface ProductDetailComponent {
    void inject(ProductDetailFragment productDetailFragment);
}
