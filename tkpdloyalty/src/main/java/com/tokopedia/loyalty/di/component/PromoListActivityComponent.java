package com.tokopedia.loyalty.di.component;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.loyalty.di.PromoActivityScope;
import com.tokopedia.loyalty.di.module.PromoListActivityModule;
import com.tokopedia.loyalty.view.activity.PromoListActivity;

import dagger.Component;

/**
 * @author anggaprasetiyo on 04/01/18.
 */
@PromoActivityScope
@Component(modules = PromoListActivityModule.class, dependencies = AppComponent.class)
public interface PromoListActivityComponent {

    void inject(PromoListActivity promoListActivity);
}
