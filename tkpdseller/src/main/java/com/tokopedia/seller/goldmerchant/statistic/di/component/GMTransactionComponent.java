package com.tokopedia.seller.goldmerchant.statistic.di.component;

import com.tokopedia.seller.base.di.component.DatePickerComponent;
import com.tokopedia.seller.goldmerchant.statistic.di.module.GMStatisticModule;
import com.tokopedia.seller.goldmerchant.statistic.di.scope.GMStatisticScope;
import com.tokopedia.seller.goldmerchant.statistic.view.fragment.GMStatisticTransactionFragment;
import com.tokopedia.seller.goldmerchant.statistic.view.fragment.GMStatisticTransactionTableFragment;

import dagger.Component;

/**
 * Created by normansyahputa on 7/6/17.
 */
@GMStatisticScope
@Component(modules = GMStatisticModule.class, dependencies = DatePickerComponent.class)
public interface GMTransactionComponent {

    void inject(GMStatisticTransactionFragment gmStatisticTransactionFragment);

    void inject(GMStatisticTransactionTableFragment gmStatisticTransactionTableFragment);
}
