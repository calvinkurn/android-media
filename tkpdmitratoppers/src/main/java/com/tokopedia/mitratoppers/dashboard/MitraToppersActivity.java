package com.tokopedia.mitratoppers.dashboard;

import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;

/**
 * Created by nathan on 8/18/17.
 */

public class MitraToppersActivity extends BaseSimpleActivity {

    @Override
    protected Fragment getNewFragment() {
        return new TestFragment();
//        return new MitraToppersFragment();
    }
}
