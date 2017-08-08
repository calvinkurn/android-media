package com.tokopedia.seller.product.di.component;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.di.scope.ActivityScope;
import com.tokopedia.seller.product.di.module.ProductScoringModule;
import com.tokopedia.seller.product.edit.view.fragment.ProductScoringDetailFragment;

import dagger.Component;

/**
 * Created by zulfikarrahman on 4/17/17.
 */

@ActivityScope
@Component (modules = ProductScoringModule.class, dependencies = AppComponent.class)
public interface ProductScoringComponent {
    void inject(ProductScoringDetailFragment productScoringDetailFragment);
}
