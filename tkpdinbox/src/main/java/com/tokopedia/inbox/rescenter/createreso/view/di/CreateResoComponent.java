package com.tokopedia.inbox.rescenter.createreso.view.di;

import com.tokopedia.core.base.di.component.AppComponent;

import dagger.Component;

/**
 * Created by yoasfs on 11/08/17.
 */


@CreateResoScope
@Component(modules = CreateResoModule.class, dependencies = AppComponent.class)
public interface CreateResoComponent {

}
