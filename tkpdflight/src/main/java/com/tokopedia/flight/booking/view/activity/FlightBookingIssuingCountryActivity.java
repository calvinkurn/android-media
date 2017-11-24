package com.tokopedia.flight.booking.view.activity;

import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.di.component.HasComponent;
import com.tokopedia.flight.FlightModuleRouter;
import com.tokopedia.flight.booking.di.DaggerFlightBookingComponent;
import com.tokopedia.flight.booking.di.FlightBookingComponent;
import com.tokopedia.flight.booking.view.fragment.FlightBookingNationalityFragment;

/**
 * Created by zulfikarrahman on 11/8/17.
 */

public class FlightBookingIssuingCountryActivity extends BaseSimpleActivity implements HasComponent<FlightBookingComponent> {

    @Override
    protected Fragment getNewFragment() {
        return new FlightBookingNationalityFragment();
    }

    @Override
    public FlightBookingComponent getComponent() {
        return DaggerFlightBookingComponent.builder()
                .flightComponent(((FlightModuleRouter) getApplication()).getFlightComponent())
                .build();
    }
}
