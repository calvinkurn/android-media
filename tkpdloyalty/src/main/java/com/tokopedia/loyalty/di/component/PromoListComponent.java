package com.tokopedia.loyalty.di.component;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.loyalty.di.PromoScope;
import com.tokopedia.loyalty.di.module.PromoListViewModule;
import com.tokopedia.loyalty.view.fragment.PromoListFragment;

import dagger.Component;

/**
 * @author anggaprasetiyo on 03/01/18.
 */
@PromoScope
@Component(modules = PromoListViewModule.class, dependencies = AppComponent.class)
public interface PromoListComponent {

    void inject(PromoListFragment promoListFragment);
}
