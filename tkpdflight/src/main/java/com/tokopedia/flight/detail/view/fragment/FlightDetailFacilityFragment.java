package com.tokopedia.flight.detail.view.fragment;

import android.os.Bundle;

import com.tokopedia.abstraction.base.view.adapter.BaseListAdapter;
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment;
import com.tokopedia.flight.detail.view.adapter.FlightDetailFacilityAdapter;
import com.tokopedia.flight.search.data.cloud.model.Route;
import com.tokopedia.flight.search.view.model.FlightSearchViewModel;

/**
 * Created by zulfikarrahman on 10/30/17.
 */

public class FlightDetailFacilityFragment extends BaseListFragment<Route> {

    public static final String EXTRA_FLIGHT_SEARCH_MODEL = "EXTRA_FLIGHT_SEARCH_MODEL";

    private FlightSearchViewModel flightSearchViewModel;

    public static FlightDetailFacilityFragment createInstance(FlightSearchViewModel flightSearchViewModel) {
        FlightDetailFacilityFragment flightDetailFacilityFragment = new FlightDetailFacilityFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_FLIGHT_SEARCH_MODEL, flightSearchViewModel);
        flightDetailFacilityFragment.setArguments(bundle);
        return flightDetailFacilityFragment;
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        flightSearchViewModel = getArguments().getParcelable(EXTRA_FLIGHT_SEARCH_MODEL);
    }

    @Override
    protected BaseListAdapter<Route> getNewAdapter() {
        return new FlightDetailFacilityAdapter();
    }

    @Override
    protected void searchForPage(int page) {
        onSearchLoaded(flightSearchViewModel.getRouteList(), flightSearchViewModel.getRouteList().size());
    }

    @Override
    public void onItemClicked(Route flightSearchData) {

    }
}
