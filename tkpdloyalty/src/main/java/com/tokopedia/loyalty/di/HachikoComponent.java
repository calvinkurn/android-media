package com.tokopedia.loyalty.di;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.loyalty.view.PromoCodeLoyaltyFragment;

import dagger.Component;

/**
 * @author anggaprasetiyo on 24/11/17.
 */
@HachikoModuleScope
@Component(modules = HachikoModule.class, dependencies = AppComponent.class)
public interface HachikoComponent {
    void inject(PromoCodeLoyaltyFragment promoCodeLoyaltyFragment);
}
