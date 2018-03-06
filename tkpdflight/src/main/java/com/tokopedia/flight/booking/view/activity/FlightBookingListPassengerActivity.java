package com.tokopedia.flight.booking.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuItem;

import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.flight.FlightModuleRouter;
import com.tokopedia.flight.R;
import com.tokopedia.flight.booking.di.DaggerFlightBookingComponent;
import com.tokopedia.flight.booking.di.FlightBookingComponent;
import com.tokopedia.flight.booking.view.adapter.viewholder.FlightBookingListPassengerViewHolder;
import com.tokopedia.flight.booking.view.fragment.FlightBookingListPassengerFragment;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingPassengerViewModel;
import com.tokopedia.flight.common.view.BaseFlightActivity;

import java.util.List;

/**
 * @author by furqan on 23/02/18.
 */

public class FlightBookingListPassengerActivity extends BaseFlightActivity implements HasComponent<FlightBookingComponent> {

    public static Intent createIntent(Context context, FlightBookingPassengerViewModel selected,
                                      String requestId) {
        Intent intent = new Intent(context, FlightBookingListPassengerActivity.class);
        intent.putExtra(FlightBookingListPassengerFragment.EXTRA_SELECTED_PASSENGER, selected);
        intent.putExtra(FlightBookingListPassengerFragment.EXTRA_REQUEST_ID, requestId);
        return intent;
    }
    @Override
    protected Fragment getNewFragment() {
        FlightBookingPassengerViewModel flightBookingPassengerViewModel = getIntent()
                .getParcelableExtra(FlightBookingListPassengerFragment.EXTRA_SELECTED_PASSENGER);
        String requestId = getIntent().getStringExtra(
                FlightBookingListPassengerFragment.EXTRA_REQUEST_ID);
        return FlightBookingListPassengerFragment.createInstance(flightBookingPassengerViewModel,
                requestId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        updateTitle(getString(R.string.pilih_penumpang_title));
    }

    @Override
    public FlightBookingComponent getComponent() {
        if (getApplication() instanceof FlightModuleRouter) {
            return DaggerFlightBookingComponent.builder()
                    .flightComponent(getFlightComponent())
                    .build();
        }
        throw new RuntimeException("Application must implement FlightModuleRouter");
    }
}
