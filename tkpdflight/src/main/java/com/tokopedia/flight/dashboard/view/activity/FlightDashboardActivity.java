package com.tokopedia.flight.dashboard.view.activity;

import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.flight.dashboard.view.fragment.FlightDashboardFragment;

/**
 * Created by nathan on 10/19/17.
 */

public class FlightDashboardActivity extends BaseSimpleActivity{

    @Override
    protected Fragment getNewFragment() {
        return FlightDashboardFragment.getInstance() ;
    }
}
