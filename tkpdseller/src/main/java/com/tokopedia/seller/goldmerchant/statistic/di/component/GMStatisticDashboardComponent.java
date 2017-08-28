package com.tokopedia.seller.goldmerchant.statistic.di.component;

import com.tokopedia.seller.goldmerchant.common.di.component.GoldMerchantComponent;
import com.tokopedia.seller.goldmerchant.statistic.di.module.GMStatisticModule;
import com.tokopedia.seller.goldmerchant.statistic.di.scope.GMStatisticScope;
import com.tokopedia.seller.goldmerchant.statistic.view.activity.GMStatisticDashboardActivity;
import com.tokopedia.seller.goldmerchant.statistic.view.fragment.GMStatisticDashboardFragment;

import dagger.Component;

/**
 * Created by normansyahputa on 6/15/17.
 */
@GMStatisticScope
@Component(modules = GMStatisticModule.class, dependencies = GoldMerchantComponent.class)
public interface GMStatisticDashboardComponent {
    void inject(GMStatisticDashboardActivity gmStatisticDashboardActivity);

    void inject(GMStatisticDashboardFragment gmStatisticDashboardFragment);
}
