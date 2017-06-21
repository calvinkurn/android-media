package com.tokopedia.seller.topads.view.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tokopedia.seller.topads.keyword.view.activity.TopAdsBaseSimpleActivity;
import com.tokopedia.seller.topads.view.fragment.TopAdsAddCreditFragment;

/**
 * Created by Nathaniel on 11/22/2016.
 */

public class TopAdsAddCreditActivity extends TopAdsBaseSimpleActivity {

    @Override
    protected Fragment getNewFragment(Bundle savedinstancestate) {
        return TopAdsAddCreditFragment.createInstance();
    }

    @Override
    protected String getTagFragment() {
        return TopAdsAddCreditActivity.class.getSimpleName();
    }

    @Override
    public String getScreenName() {
        return null;
    }
}