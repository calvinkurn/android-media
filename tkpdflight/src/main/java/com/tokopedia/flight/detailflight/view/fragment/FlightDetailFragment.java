package com.tokopedia.flight.detailflight.view.fragment;

import com.tokopedia.abstraction.base.view.adapter.BaseListAdapter;
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment;
import com.tokopedia.flight.detailflight.view.adapter.FlightDetailAdapter;
import com.tokopedia.flight.search.data.cloud.model.Route;
import com.tokopedia.flight.search.view.model.FlightSearchViewModel;

/**
 * Created by zulfikarrahman on 10/30/17.
 */

public class FlightDetailFragment extends BaseListFragment<Route> {

    public static FlightDetailFragment createInstance() {
        return new FlightDetailFragment();
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
        return new FlightDetailAdapter();
    }

    @Override
    protected void searchForPage(int page) {

    }

    @Override
    public void onItemClicked(Route flightSearchData) {

    }
}
