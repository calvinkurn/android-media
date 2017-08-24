package com.tokopedia.seller.topads.dashboard.view.activity;

import android.support.v4.app.Fragment;

import com.tokopedia.seller.base.view.activity.BaseSimpleActivity;
import com.tokopedia.seller.topads.dashboard.view.fragment.TopAdsAddCreditFragment;

/**
 * Created by Nathaniel on 11/22/2016.
 */

public class TopAdsAddCreditActivity extends BaseSimpleActivity {

    @Override
    protected Fragment getNewFragment() {
        return TopAdsAddCreditFragment.createInstance();
    }

    @Override
    public String getScreenName() {
        return null;
    }
}