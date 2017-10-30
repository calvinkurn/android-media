package com.tokopedia.flight.detailflight.view.fragment;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;

/**
 * Created by zulfikarrahman on 10/30/17.
 */

public class FlightDetailPriceFragment extends BaseDaggerFragment {
    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {

    }

    public static FlightDetailPriceFragment createInstance() {
        return new FlightDetailPriceFragment();
    }
}
