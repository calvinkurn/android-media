package com.tokopedia.seller.topads.view.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tokopedia.core.app.TActivity;
import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.keyword.view.activity.TopAdsBaseActivity;
import com.tokopedia.seller.topads.view.fragment.TopAdsAddCreditFragment;

/**
 * Created by Nathaniel on 11/22/2016.
 */

public class TopAdsAddCreditActivity extends TopAdsBaseActivity {

    @Override
    protected Fragment getNewFragment(Bundle savedinstancestate) {
        return TopAdsAddCreditFragment.createInstance();
    }

    @Override
    public String getScreenName() {
        return null;
    }
}