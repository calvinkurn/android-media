package com.tokopedia.loyalty.di.component;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.loyalty.di.LoyaltyScope;
import com.tokopedia.loyalty.di.module.PromoCouponViewModule;
import com.tokopedia.loyalty.view.fragment.PromoCouponFragment;

import dagger.Component;

/**
 * @author anggaprasetiyo on 29/11/17.
 */
@LoyaltyScope
@Component(modules = PromoCouponViewModule.class, dependencies = BaseAppComponent.class)
public interface PromoCouponComponent {
    void inject(PromoCouponFragment promoCouponFragment);
}
