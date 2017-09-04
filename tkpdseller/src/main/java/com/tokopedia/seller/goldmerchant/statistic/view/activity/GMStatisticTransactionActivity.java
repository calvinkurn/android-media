package com.tokopedia.seller.goldmerchant.statistic.view.activity;

import android.support.v4.app.Fragment;

import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.seller.SellerModuleRouter;
import com.tokopedia.seller.base.view.activity.BaseSimpleActivity;
import com.tokopedia.seller.goldmerchant.common.di.component.GoldMerchantComponent;
import com.tokopedia.seller.goldmerchant.statistic.view.fragment.GMStatisticTransactionFragment;

/**
 * @author nathan on 7/5/17.
 */

public class GMStatisticTransactionActivity extends BaseSimpleActivity implements HasComponent<GoldMerchantComponent> {

    @Override
    protected Fragment getNewFragment() {
        return GMStatisticTransactionFragment.createInstance();
    }

    @Override
    public GoldMerchantComponent getComponent() {
        return ((SellerModuleRouter) getApplication()).getGoldMerchantComponent();
    }
}
