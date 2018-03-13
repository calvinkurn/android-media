package com.tokopedia.flight.passenger.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.flight.R;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingPassengerViewModel;
import com.tokopedia.flight.passenger.di.FlightPassengerComponent;
import com.tokopedia.flight.passenger.view.presenter.FlightPassengerUpdateContract;
import com.tokopedia.flight.passenger.view.presenter.FlightPassengerUpdatePresenter;

import javax.inject.Inject;

/**
 * @author by furqan on 12/03/18.
 */

public class FlightPassengerUpdateFragment extends BaseDaggerFragment implements FlightPassengerUpdateContract.View {

    public static final String EXTRA_PASSENGER_ID = "EXTRA_PASSENGER_ID";

    @Inject
    FlightPassengerUpdatePresenter presenter;
    private FlightBookingPassengerViewModel flightBookingPassengerViewModel;



    public FlightPassengerUpdateFragment() {
    }

    public static FlightPassengerUpdateFragment newInstance(String passengerId) {
        FlightPassengerUpdateFragment flightPassengerUpdateFragment = new FlightPassengerUpdateFragment();
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_PASSENGER_ID, passengerId);
        flightPassengerUpdateFragment.setArguments(bundle);
        return flightPassengerUpdateFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_flight_passenger_update, container, false);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.attachView(this);
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {
        getComponent(FlightPassengerComponent.class).inject(this);
    }

    @Override
    public String getPassengerId() {
        if (getArguments() != null) {
            return getArguments().getString(EXTRA_PASSENGER_ID);
        }
        return "";
    }

    @Override
    public FlightBookingPassengerViewModel getCurrentPassengerViewModel() {
        return flightBookingPassengerViewModel;
    }

    @Override
    public void setPassengerViewModel(FlightBookingPassengerViewModel flightBookingPassengerViewModel) {
        this.flightBookingPassengerViewModel = flightBookingPassengerViewModel;
    }
}
