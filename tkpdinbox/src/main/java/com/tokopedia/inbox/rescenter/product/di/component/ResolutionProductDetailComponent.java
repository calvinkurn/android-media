package com.tokopedia.inbox.rescenter.product.di.component;

import com.tokopedia.inbox.rescenter.detailv2.di.component.ResolutionDetailComponent;
import com.tokopedia.inbox.rescenter.product.di.module.ResolutionProductDetailModule;
import com.tokopedia.inbox.rescenter.product.ProductDetailFragment;
import com.tokopedia.inbox.rescenter.product.di.scope.ResolutionProductDetailScope;

import dagger.Component;

/**
 * Created by hangnadi on 4/17/17.
 */
@ResolutionProductDetailScope
@Component(modules = ResolutionProductDetailModule.class, dependencies = ResolutionDetailComponent.class)
public interface ResolutionProductDetailComponent {

    void inject(ProductDetailFragment fragment);

}
