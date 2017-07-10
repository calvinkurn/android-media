package com.tokopedia.seller.gmstat.di.component;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.seller.gmstat.di.module.GMStatModule;
import com.tokopedia.seller.gmstat.di.scope.GMStatScope;
import com.tokopedia.seller.gmstat.views.BaseGMStatActivity;

import dagger.Component;

/**
 * Created by normansyahputa on 6/15/17.
 */
@GMStatScope
@Component(modules = GMStatModule.class, dependencies = AppComponent.class)
public interface GMStatComponent {
    void inject(BaseGMStatActivity baseGMStatActivity);
}
