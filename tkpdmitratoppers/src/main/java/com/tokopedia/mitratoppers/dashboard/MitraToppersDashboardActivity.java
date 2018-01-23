package com.tokopedia.mitratoppers.dashboard;

import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.mitratoppers.preapprove.view.MitraToppersPreApproveFragment;

/**
 * Created by nathan on 8/18/17.
 */

public class MitraToppersDashboardActivity extends BaseSimpleActivity {

    @Override
    protected Fragment getNewFragment() {
        return new MitraToppersPreApproveFragment();
//        return new MitraToppersDashboardFragment();
    }
}
