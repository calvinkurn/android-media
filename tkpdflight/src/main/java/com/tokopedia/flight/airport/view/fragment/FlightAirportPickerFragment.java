package com.tokopedia.flight.airport.view.fragment;

import com.tokopedia.abstraction.base.view.adapter.BaseListAdapter;
import com.tokopedia.abstraction.base.view.fragment.BaseSearchListFragment;
import com.tokopedia.flight.R;
import com.tokopedia.flight.airport.data.source.db.model.FlightAirportDB;
import com.tokopedia.flight.airport.view.adapter.FlightAirportAdapter;
import com.tokopedia.flight.airport.view.presenter.FlightAirportPickerPresenter;
import com.tokopedia.flight.airport.view.presenter.FlightAirportPickerPresenterImpl;

import javax.inject.Inject;

/**
 * Created by nathan on 10/19/17.
 */

public class FlightAirportPickerFragment extends BaseSearchListFragment<FlightAirportDB>{

    @Inject
    FlightAirportPickerPresenter flightAirportPickerPresenter;

    public static FlightAirportPickerFragment getInstance() {
        return new FlightAirportPickerFragment();
    }

    @Override
    protected void initInjector() {

    }

    @Override
    protected BaseListAdapter<FlightAirportDB> getNewAdapter() {
        return new FlightAirportAdapter();
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_flight_airport_picker;
    }

    @Override
    public void onItemClicked(FlightAirportDB flightAirportDB) {

    }

    @Override
    protected void searchForPage(int page) {

    }

    @Override
    protected String getScreenName() {
        return null;
    }
}