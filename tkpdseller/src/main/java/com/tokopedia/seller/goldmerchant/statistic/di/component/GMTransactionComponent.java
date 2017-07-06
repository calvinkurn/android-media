package com.tokopedia.seller.goldmerchant.statistic.di.component;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.seller.goldmerchant.statistic.di.module.GMStatisticTransactionModule;
import com.tokopedia.seller.goldmerchant.statistic.di.scope.GMStatisticTransactionScope;
import com.tokopedia.seller.goldmerchant.statistic.view.fragment.GMStatisticTransactionFragment;

import dagger.Component;

/**
 * Created by normansyahputa on 7/6/17.
 */
@GMStatisticTransactionScope
@Component(modules = GMStatisticTransactionModule.class, dependencies = AppComponent.class)
public interface GMTransactionComponent {

    void inject(GMStatisticTransactionFragment gmStatisticTransactionFragment);
}
