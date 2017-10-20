package com.tokopedia.flight.airport.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.flight.R;

/**
 * Created by nathan on 10/19/17.
 */

public class FlightAirportPickerFragment extends BaseDaggerFragment{

    public static FlightAirportPickerFragment getInstance() {
        return new FlightAirportPickerFragment();
    }

    @Override
    protected void initInjector() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_flight_dashboard, container, false);
        return view;
    }

    @Override
    protected String getScreenName() {
        return null;
    }
}