package com.tokopedia.flight.dashboard.view.fragment;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;

/**
 * Created by nathan on 10/19/17.
 */

public class FlightDashboardFragment extends BaseDaggerFragment{

    public static FlightDashboardFragment getInstance() {
        return new FlightDashboardFragment();
    }

    @Override
    protected void initInjector() {

    }

    @Override
    protected String getScreenName() {
        return null;
    }
}
