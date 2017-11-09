package com.tokopedia.flight.booking.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.di.component.HasComponent;
import com.tokopedia.flight.FlightModuleRouter;
import com.tokopedia.flight.booking.di.DaggerFlightBookingComponent;
import com.tokopedia.flight.booking.di.FlightBookingComponent;
import com.tokopedia.flight.booking.view.fragment.FlightBookingFragment;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingTripViewModel;

/**
 * Created by alvarisi on 11/6/17.
 */

public class FlightBookingActivity extends BaseSimpleActivity implements HasComponent<FlightBookingComponent> {
    private static final String EXTRA_PASS_DATA = "EXTRA_PASS_DATA";

    public static Intent getCallingIntent(Activity activity, FlightBookingTripViewModel flightBookingTripViewModel) {
        Intent intent = new Intent(activity, FlightBookingActivity.class);
        intent.putExtra(EXTRA_PASS_DATA, flightBookingTripViewModel);
        return intent;
    }

    @Override
    protected Fragment getNewFragment() {
        FlightBookingTripViewModel flightBookingTripViewModel = getIntent().getParcelableExtra(EXTRA_PASS_DATA);
        return FlightBookingFragment.newInstance(flightBookingTripViewModel);
    }

    @Override
    protected boolean isToolbarWhite() {
        return true;
    }

    @Override
    public FlightBookingComponent getComponent() {
        if (getApplication() instanceof FlightModuleRouter) {
            return DaggerFlightBookingComponent.builder()
                    .flightComponent(((FlightModuleRouter) getApplication()).getFlightComponent())
                    .build();
        }
        throw new RuntimeException("Application must implement FlightModuleRouter");
    }
}
