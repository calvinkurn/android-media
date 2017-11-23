package com.tokopedia.seller.fintech.mitratoppers.view.activity;

import android.support.v4.app.Fragment;

import com.tokopedia.seller.base.view.activity.BaseSimpleActivity;
import com.tokopedia.seller.fintech.mitratoppers.view.fragment.MitraToppersFragment;

/**
 * Created by nathan on 8/18/17.
 */

public class MitraToppersActivity extends BaseSimpleActivity {

    @Override
    protected Fragment getNewFragment() {
        return new MitraToppersFragment();
    }
}
