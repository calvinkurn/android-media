package com.tokopedia.seller.topads.dashboard.di.component;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.seller.topads.dashboard.di.module.TopAdsCreatePromoModule;
import com.tokopedia.seller.topads.dashboard.di.scope.TopAdsDashboardScope;
import com.tokopedia.seller.topads.dashboard.view.fragment.TopAdsNewCostWithoutGroupFragment;
import com.tokopedia.seller.topads.dashboard.view.fragment.TopAdsNewProductListExistingGroupFragment;
import com.tokopedia.seller.topads.dashboard.view.fragment.TopAdsNewScheduleFragment;
import com.tokopedia.seller.topads.dashboard.view.fragment.TopAdsNewScheduleNewGroupFragment;

import dagger.Component;

/**
 * Created by zulfikarrahman on 8/13/17.
 */

@TopAdsDashboardScope
@Component(modules = TopAdsCreatePromoModule.class, dependencies = AppComponent.class)
public interface TopAdsCreatePromoComponent {
    void inject(TopAdsNewScheduleNewGroupFragment topAdsNewScheduleFragment);
    void inject(TopAdsNewCostWithoutGroupFragment topAdsNewCostWithoutGroupFragment);
    void inject(TopAdsNewProductListExistingGroupFragment topAdsNewProductListExistingGroupFragment);
}
