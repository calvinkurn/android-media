package com.tokopedia.seller.gmsubscribe.di.component;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.seller.gmsubscribe.di.module.GmSubscribeModule;
import com.tokopedia.seller.gmsubscribe.di.scope.GmSubscribeScope;
import com.tokopedia.seller.gmsubscribe.view.presenter.GmCheckoutPresenterImpl;
import com.tokopedia.seller.gmsubscribe.view.presenter.GmHomePresenterImpl;
import com.tokopedia.seller.gmsubscribe.view.presenter.GmProductPresenterImpl;

import dagger.Component;

/**
 * @author sebastianuskh on 5/2/17.
 */
@GmSubscribeScope
@Component(modules = GmSubscribeModule.class, dependencies = AppComponent.class)
public interface GmSubscribeComponent {
    GmHomePresenterImpl getHomePresenter();

    GmProductPresenterImpl getProductPresenter();

    GmCheckoutPresenterImpl getCheckoutPresenter();
}
