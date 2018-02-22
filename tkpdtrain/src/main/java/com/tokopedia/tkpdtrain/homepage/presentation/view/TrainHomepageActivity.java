package com.tokopedia.tkpdtrain.homepage.presentation.view;

import android.support.v4.app.Fragment;

import com.tokopedia.tkpdtrain.common.presentation.TrainBaseActivity;


public class TrainHomepageActivity extends TrainBaseActivity {

    @Override
    protected Fragment getNewFragment() {
        return new TrainHomepageFragment();
    }

}
