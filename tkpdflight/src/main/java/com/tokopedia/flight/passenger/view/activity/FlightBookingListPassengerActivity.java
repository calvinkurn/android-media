package com.tokopedia.flight.passenger.view.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.Menu;

import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingPassengerViewModel;
import com.tokopedia.flight.common.view.BaseFlightActivity;
import com.tokopedia.flight.passenger.di.DaggerFlightPassengerComponent;
import com.tokopedia.flight.passenger.di.FlightPassengerComponent;
import com.tokopedia.flight.passenger.view.fragment.FlightBookingListPassengerFragment;

/**
 * @author by furqan on 23/02/18.
 */

public class FlightBookingListPassengerActivity extends BaseFlightActivity implements HasComponent<FlightPassengerComponent> {

    public static Intent createIntent(Context context, FlightBookingPassengerViewModel selected,
                                      String requestId, String departureDate) {
        Intent intent = new Intent(context, FlightBookingListPassengerActivity.class);
        intent.putExtra(FlightBookingListPassengerFragment.EXTRA_SELECTED_PASSENGER, selected);
        intent.putExtra(FlightBookingListPassengerFragment.EXTRA_REQUEST_ID, requestId);
        intent.putExtra(FlightBookingListPassengerFragment.EXTRA_DEPARTURE_DATE, departureDate);
        return intent;
    }

    @Override
    protected Fragment getNewFragment() {
        FlightBookingPassengerViewModel flightBookingPassengerViewModel = getIntent()
                .getParcelableExtra(FlightBookingListPassengerFragment.EXTRA_SELECTED_PASSENGER);
        String requestId = getIntent().getStringExtra(
                FlightBookingListPassengerFragment.EXTRA_REQUEST_ID);
        String departureDate = getIntent().getStringExtra(
                FlightBookingListPassengerFragment.EXTRA_DEPARTURE_DATE);
        return FlightBookingListPassengerFragment.createInstance(flightBookingPassengerViewModel,
                requestId, departureDate);
    }

    @Override
    public FlightPassengerComponent getComponent() {
        return DaggerFlightPassengerComponent.builder()
                .flightComponent(getFlightComponent())
                .build();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }
}
