package com.tokopedia.inbox.rescenter.product.di.component;

import com.tokopedia.inbox.rescenter.detailv2.di.component.ResolutionDetailComponent;
import com.tokopedia.inbox.rescenter.product.di.module.ResolutionProductListModule;
import com.tokopedia.inbox.rescenter.product.ListProductFragment;
import com.tokopedia.inbox.rescenter.product.di.scope.ResolutionProductListScope;

import dagger.Component;

/**
 * Created by hangnadi on 4/17/17.
 */
@ResolutionProductListScope
@Component(modules = ResolutionProductListModule.class, dependencies = ResolutionDetailComponent.class)
public interface ResolutionProductListComponent {

    void inject(ListProductFragment fragment);
}
