package com.tokopedia.seller.goldmerchant.statistic.view.activity;

import android.support.v4.app.Fragment;

import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.seller.SellerModuleRouter;
import com.tokopedia.seller.base.view.activity.BaseSimpleActivity;
import com.tokopedia.seller.goldmerchant.common.di.component.GoldMerchantComponent;
import com.tokopedia.seller.goldmerchant.statistic.view.fragment.GMStatisticTransactionTableFragment;

/**
 * Created by normansyahputa on 7/13/17.
 */

public class GMStatisticTransactionTableActivity extends BaseSimpleActivity implements HasComponent<GoldMerchantComponent> {

    @Override
    protected Fragment getNewFragment() {
        return GMStatisticTransactionTableFragment.createInstance();
    }

    @Override
    public GoldMerchantComponent getComponent() {
        return ((SellerModuleRouter) getApplication()).getGoldMerchantComponent(getActivityModule());
    }
}
