package com.tokopedia.seller.opportunity.di.component;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.seller.opportunity.di.module.OpportunityModule;
import com.tokopedia.seller.opportunity.di.scope.OpportunityScope;
import com.tokopedia.seller.opportunity.fragment.OpportunityDetailFragment;
import com.tokopedia.seller.opportunity.fragment.OpportunityListFragment;
import com.tokopedia.seller.opportunity.fragment.OpportunityTncFragment;

import dagger.Component;

/**
 * Created by normansyahputa on 1/10/18.
 */
@OpportunityScope
@Component(modules = OpportunityModule.class, dependencies = AppComponent.class)
public interface OpportunityComponent {
    void inject(OpportunityListFragment opportunityListFragment);

    void inject(OpportunityDetailFragment opportunityDetailFragment);

    void inject(OpportunityTncFragment opportunityTncFragment);
}
