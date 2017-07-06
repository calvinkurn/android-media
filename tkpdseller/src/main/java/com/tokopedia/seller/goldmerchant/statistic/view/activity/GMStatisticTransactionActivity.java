package com.tokopedia.seller.goldmerchant.statistic.view.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.app.BaseActivity;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.seller.R;
import com.tokopedia.seller.goldmerchant.statistic.view.fragment.GMStatisticTransactionFragment;

/**
 * @author nathan on 7/5/17.
 */

public class GMStatisticTransactionActivity extends BaseActivity
        implements HasComponent<AppComponent> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_product_add);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        if (!CommonUtils.checkNotNull(gmStatisticTransactionFragment())) {
            getSupportFragmentManager().beginTransaction().disallowAddToBackStack()
                    .add(R.id.container, GMStatisticTransactionFragment.createInstance(), GMStatisticTransactionFragment.TAG)
                    .commit();
        }
    }

    @Override
    public AppComponent getComponent() {
        return getApplicationComponent();
    }


    public GMStatisticTransactionFragment gmStatisticTransactionFragment() {
        Fragment fragmentByTag = getSupportFragmentManager().findFragmentByTag(GMStatisticTransactionFragment.TAG);
        return (fragmentByTag != null && fragmentByTag instanceof GMStatisticTransactionFragment) ?
                (GMStatisticTransactionFragment) fragmentByTag : null;
    }
}
