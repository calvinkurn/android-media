package com.tokopedia.flight.detail.view.fragment;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.flight.search.view.model.FlightSearchViewModel;

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

    public static FlightDetailPriceFragment createInstance(FlightSearchViewModel flightSearchViewModel) {
        return new FlightDetailPriceFragment();
    }
}
