package com.tokopedia.flight.dashboard.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.flight.R;
import com.tokopedia.flight.airport.view.activity.FlightAirportPickerActivity;
import com.tokopedia.flight.dashboard.view.activity.SelectFlightPassengerActivity;
import com.tokopedia.flight.dashboard.view.fragment.passdata.SelectFlightPassengerPassData;
import com.tokopedia.flight.dashboard.view.widget.TextInputView;

/**
 * Created by nathan on 10/19/17.
 */

public class FlightDashboardFragment extends Fragment {

    private static final int REQUEST_CODE_AIRPORT_DEPARTURE = 1;
    private static final int REQUEST_CODE_AIRPORT_ARRIVAL = 2;
    private static final int REQUEST_CODE_AIRPORT_PASSENGER = 3;

    public static FlightDashboardFragment getInstance() {
        return new FlightDashboardFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_flight_dashboard, container, false);
        TextInputView airportDepartureTextInputView = (TextInputView) view.findViewById(R.id.text_input_view_departure_airport);
        TextInputView airportArrivalTextInputView = (TextInputView) view.findViewById(R.id.text_input_view_arrival_airport);
        TextInputView passengerTextInputView = (TextInputView) view.findViewById(R.id.text_input_view_passenger);
        airportDepartureTextInputView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = FlightAirportPickerActivity.createInstance(getActivity());
                getActivity().startActivityForResult(intent, REQUEST_CODE_AIRPORT_DEPARTURE);
            }
        });
        airportArrivalTextInputView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = FlightAirportPickerActivity.createInstance(getActivity());
                getActivity().startActivityForResult(intent, REQUEST_CODE_AIRPORT_ARRIVAL);
            }
        });
        passengerTextInputView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectFlightPassengerPassData passData = new SelectFlightPassengerPassData.Builder()
                        .build();
                Intent intent = SelectFlightPassengerActivity.getCallingIntent(getActivity(), passData);
                getActivity().startActivityForResult(intent, REQUEST_CODE_AIRPORT_PASSENGER);
            }
        });

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_flight_dashboard, menu);
        if (getActivity() instanceof BaseSimpleActivity) {
            ((BaseSimpleActivity) getActivity()).updateOptionMenuColor(menu);
        }
    }

}