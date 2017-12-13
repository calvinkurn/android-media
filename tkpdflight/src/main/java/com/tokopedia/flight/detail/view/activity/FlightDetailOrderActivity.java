package com.tokopedia.flight.detail.view.activity;

import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.flight.detail.view.fragment.FlightDetailOrderFragment;

/**
 * Created by zulfikarrahman on 12/12/17.
 */

public class FlightDetailOrderActivity extends BaseSimpleActivity {
    @Override
    protected Fragment getNewFragment() {
        return FlightDetailOrderFragment.createInstance();
    }
}
