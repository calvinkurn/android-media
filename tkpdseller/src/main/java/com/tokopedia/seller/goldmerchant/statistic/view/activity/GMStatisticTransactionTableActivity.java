package com.tokopedia.seller.goldmerchant.statistic.view.activity;

import android.support.v4.app.Fragment;

import com.tokopedia.seller.base.view.activity.BaseSimpleActivity;
import com.tokopedia.seller.goldmerchant.statistic.view.fragment.GMStatisticTransactionTableFragment;

/**
 * Created by normansyahputa on 7/13/17.
 */

public class GMStatisticTransactionTableActivity extends BaseSimpleActivity {
    @Override
    protected Fragment getNewFragment() {
        return GMStatisticTransactionTableFragment.createInstance();
    }

    @Override
    protected String getTagFragment() {
        return GMStatisticTransactionTableFragment.TAG;
    }
}
