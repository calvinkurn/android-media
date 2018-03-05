package com.tokopedia.tkpdtrain.station.presentation;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;

/**
 * Created by alvarisi on 3/5/18.
 */

public class TrainStationsActivity extends BaseSimpleActivity {
    public static Intent getCallingIntent(Activity activity) {
        Intent intent = new Intent(activity, TrainStationsActivity.class);
        return intent;
    }

    @Override
    protected Fragment getNewFragment() {
        return TrainStationsFragment.newInstance();
    }
}
