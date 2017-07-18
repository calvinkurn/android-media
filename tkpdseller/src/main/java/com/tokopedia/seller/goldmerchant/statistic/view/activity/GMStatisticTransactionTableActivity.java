package com.tokopedia.seller.goldmerchant.statistic.view.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.seller.SellerModuleRouter;
import com.tokopedia.seller.base.view.activity.BaseSimpleActivity;
import com.tokopedia.seller.goldmerchant.common.di.component.GoldMerchantComponent;
import com.tokopedia.seller.goldmerchant.statistic.view.fragment.GMStatisticTransactionTableFragment;
import com.tokopedia.seller.goldmerchant.statistic.view.presenter.GMStatisticTransactionTableView;

/**
 * Created by normansyahputa on 7/13/17.
 */

public class GMStatisticTransactionTableActivity extends BaseSimpleActivity implements HasComponent<GoldMerchantComponent> {
    long startDate = -1;
    long endDate = -1;

    @Override
    protected void setupFragment(Bundle savedinstancestate) {
        if (savedinstancestate == null && getIntent() != null) {
            startDate = getIntent().getLongExtra(GMStatisticTransactionTableView.START_DATE, -1);
            endDate = getIntent().getLongExtra(GMStatisticTransactionTableView.END_DATE, -1);
        }
        super.setupFragment(savedinstancestate);
    }

    @Override
    protected Fragment getNewFragment() {
        return GMStatisticTransactionTableFragment.createInstance(startDate, endDate);
    }

    @Override
    protected String getTagFragment() {
        return GMStatisticTransactionTableFragment.TAG;
    }

    @Override
    public GoldMerchantComponent getComponent() {
        return ((SellerModuleRouter) getApplication()).getGoldMerchantComponent(getActivityModule());
    }
}
