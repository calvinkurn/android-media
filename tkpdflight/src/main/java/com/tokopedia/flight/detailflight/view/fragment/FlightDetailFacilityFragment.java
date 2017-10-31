package com.tokopedia.flight.detailflight.view.fragment;

import com.tokopedia.abstraction.base.view.adapter.BaseListAdapter;
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment;
import com.tokopedia.flight.detailflight.view.adapter.FlightDetailFacilityAdapter;
import com.tokopedia.flight.search.data.cloud.model.Route;

/**
 * Created by zulfikarrahman on 10/30/17.
 */

public class FlightDetailFacilityFragment extends BaseListFragment<Route> {

    public static FlightDetailFacilityFragment createInstance() {
        return new FlightDetailFacilityFragment();
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {

    }

    @Override
    protected BaseListAdapter<Route> getNewAdapter() {
        return new FlightDetailFacilityAdapter();
    }

    @Override
    protected void searchForPage(int page) {

    }

    @Override
    public void onItemClicked(Route flightSearchData) {

    }
}
