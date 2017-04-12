package com.tokopedia.inbox.rescenter.detailv2.di.component;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.inbox.rescenter.detailv2.di.module.ResolutionModule;
import com.tokopedia.inbox.rescenter.detailv2.di.scope.ResolutionDetailScope;
import com.tokopedia.inbox.rescenter.detailv2.view.DetailResCenterFragment;

import dagger.Component;

/**
 * Created by hangnadi on 4/11/17.
 */
@ResolutionDetailScope
@Component(modules = ResolutionModule.class, dependencies = AppComponent.class)
public interface ResolutionDetailComponent {
    void inject(DetailResCenterFragment fragment);
}
