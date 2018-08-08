package com.tokopedia.tkpdpdp.estimasiongkir;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.tkpdpdp.estimasiongkir.di.RatesEstimationComponent;
import com.tokopedia.tkpdpdp.estimasiongkir.di.DaggerRatesEstimationComponent;
import com.tokopedia.tkpdpdp.estimasiongkir.di.RatesEstimationModule;

public class RatesEstimationDetailActivity extends BaseSimpleActivity implements HasComponent<RatesEstimationComponent> {

    public static Intent createIntent(Context context){
        return new Intent(context, RatesEstimationDetailActivity.class);
    }

    @Override
    protected Fragment getNewFragment() {
        return RatesEstimationDetailFragment.createInstance();
    }

    @Override
    public RatesEstimationComponent getComponent() {
        return DaggerRatesEstimationComponent.builder()
                .baseAppComponent(((BaseMainApplication) getApplication()).getBaseAppComponent())
                .ratesEstimationModule(new RatesEstimationModule()).build();
    }
}
