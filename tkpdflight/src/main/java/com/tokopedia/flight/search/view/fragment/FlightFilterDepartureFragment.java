package com.tokopedia.flight.search.view.fragment;

import android.content.Context;
import android.os.Bundle;

import com.tokopedia.abstraction.base.view.adapter.BaseListAdapter;
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment;
import com.tokopedia.flight.R;
import com.tokopedia.flight.search.adapter.FlightSearchAdapter;
import com.tokopedia.flight.search.view.fragment.flightinterface.OnFlightFilterListener;
import com.tokopedia.flight.search.view.model.FlightSearchViewModel;

/**
 * Created by nathan on 10/27/17.
 */

public class FlightFilterDepartureFragment extends BaseListFragment<FlightSearchViewModel> {
    public static final String TAG = FlightFilterDepartureFragment.class.getSimpleName();

    private OnFlightFilterListener listener;

    public static FlightFilterDepartureFragment newInstance() {

        Bundle args = new Bundle();

        FlightFilterDepartureFragment fragment = new FlightFilterDepartureFragment();
        fragment.setArguments(args);
        return fragment;
    }

    protected int getFragmentLayout() {
        return R.layout.fragment_flight_filter_departure;
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {

    }

    @Override
    protected BaseListAdapter<FlightSearchViewModel> getNewAdapter() {
        return new FlightSearchAdapter();
    }

    @Override
    protected void searchForPage(int page) {

    }

    @Override
    public void onItemClicked(FlightSearchViewModel flightSearchViewModel) {

    }

    @Override
    protected void onAttachActivity(Context context) {
        listener = (OnFlightFilterListener) context;
    }
}
