package com.tokopedia.seller.reputation.di.component;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.seller.reputation.di.module.ReputationModule;
import com.tokopedia.seller.reputation.di.scope.ReputationScope;
import com.tokopedia.seller.reputation.view.fragment.SellerReputationFragment;

import dagger.Component;

/**
 * @author normansyahputa on 3/15/17.
 */
@ReputationScope
@Component(modules = ReputationModule.class, dependencies = AppComponent.class)
public interface ReputationComponent {
    void inject(SellerReputationFragment sellerReputationFragment);
}
