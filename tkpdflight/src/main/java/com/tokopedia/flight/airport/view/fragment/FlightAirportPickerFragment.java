package com.tokopedia.flight.airport.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.adapter.BaseListAdapter;
import com.tokopedia.abstraction.base.view.fragment.BaseSearchListFragment;
import com.tokopedia.flight.R;
import com.tokopedia.flight.airport.data.source.db.FlightAirportDB;

/**
 * Created by nathan on 10/19/17.
 */

public class FlightAirportPickerFragment extends BaseSearchListFragment<FlightAirportDB>{

    public static FlightAirportPickerFragment getInstance() {
        return new FlightAirportPickerFragment();
    }

    @Override
    protected void initInjector() {

    }

    @Override
    protected BaseListAdapter<FlightAirportDB> getNewAdapter() {
        return null;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_flight_airport_picker, container, false);
        return view;
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