package com.tokopedia.flight.dashboard.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.flight.dashboard.view.fragment.SelectFlightPassengerFragment;
import com.tokopedia.flight.dashboard.view.fragment.passdata.SelectFlightPassengerPassData;

public class SelectFlightPassengerActivity extends BaseSimpleActivity {
    private static final String EXTRA_PASS_DATA = "EXTRA_PASS_DATA";

    public static Intent getCallingIntent(Activity activity, SelectFlightPassengerPassData passData) {
        Intent intent = new Intent(activity, SelectFlightPassengerActivity.class);
        intent.putExtra(EXTRA_PASS_DATA, passData);
        return intent;
    }

    @Override
    protected Fragment getNewFragment() {
        SelectFlightPassengerPassData passengerPassData = getIntent().getParcelableExtra(EXTRA_PASS_DATA);
        return SelectFlightPassengerFragment.newInstance(passengerPassData);
    }
}
