package com.tokopedia.gm.statistic.di.component;

import com.tokopedia.gm.common.di.component.GMComponent;
import com.tokopedia.gm.statistic.di.module.GMStatisticModule;
import com.tokopedia.gm.statistic.di.scope.GMStatisticScope;
import com.tokopedia.gm.statistic.view.fragment.GMStatisticTransactionFragment;
import com.tokopedia.gm.statistic.view.fragment.GMStatisticTransactionTableFragment;

import dagger.Component;

/**
 * Created by normansyahputa on 7/6/17.
 */
@GMStatisticScope
@Component(modules = GMStatisticModule.class, dependencies = GMComponent.class)
public interface GMStatisticTransactionComponent {

    void inject(GMStatisticTransactionFragment gmStatisticTransactionFragment);

    void inject(GMStatisticTransactionTableFragment gmStatisticTransactionTableFragment);
}
