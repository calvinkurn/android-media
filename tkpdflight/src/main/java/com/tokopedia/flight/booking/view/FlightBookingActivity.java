package com.tokopedia.flight.booking.view;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;

/**
 * Created by alvarisi on 11/6/17.
 */

public class FlightBookingActivity extends BaseSimpleActivity {

    public static Intent getCallingIntent(Activity activity) {
        return new Intent(activity, FlightBookingActivity.class);
    }

    @Override
    protected Fragment getNewFragment() {
        return FlightBookingFragment.newInstance();
    }
}
