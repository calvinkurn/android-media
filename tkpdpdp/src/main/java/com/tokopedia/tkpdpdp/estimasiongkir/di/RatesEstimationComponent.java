package com.tokopedia.tkpdpdp.estimasiongkir.di;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.tkpdpdp.estimasiongkir.RatesEstimationDetailFragment;

import dagger.Component;

@RatesEstimationScope
@Component(modules = RatesEstimationModule.class, dependencies = BaseAppComponent.class)
public interface RatesEstimationComponent {
    void inject(RatesEstimationDetailFragment fragment);
}
