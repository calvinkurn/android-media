package com.tokopedia.seller.product.edit.di.component;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.di.scope.ActivityScope;
import com.tokopedia.seller.product.common.di.component.ProductComponent;
import com.tokopedia.seller.product.edit.di.module.ProductScoringModule;
import com.tokopedia.seller.product.edit.view.fragment.ProductScoringDetailFragment;

import dagger.Component;

/**
 * Created by zulfikarrahman on 4/17/17.
 */

@ActivityScope
@Component (modules = ProductScoringModule.class, dependencies = ProductComponent.class)
public interface ProductScoringComponent {
    void inject(ProductScoringDetailFragment productScoringDetailFragment);
}
