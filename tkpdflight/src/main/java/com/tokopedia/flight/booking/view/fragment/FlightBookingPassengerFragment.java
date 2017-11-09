package com.tokopedia.flight.booking.view.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.flight.R;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingPassengerViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class FlightBookingPassengerFragment extends BaseDaggerFragment {
    public static final String EXTRA_PASSENGER = "EXTRA_PASSENGER";

    public static FlightBookingPassengerFragment newInstance(FlightBookingPassengerViewModel viewModel) {
        FlightBookingPassengerFragment fragment = new FlightBookingPassengerFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_PASSENGER, viewModel);
        fragment.setArguments(bundle);
        return fragment;
    }

    public FlightBookingPassengerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_flight_booking_passenger, container, false);
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {

    }
}
