package com.tokopedia.flight.review;

import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.di.component.HasComponent;
import com.tokopedia.flight.FlightModuleRouter;
import com.tokopedia.flight.booking.di.DaggerFlightBookingComponent;
import com.tokopedia.flight.booking.di.FlightBookingComponent;

/**
 * Created by zulfikarrahman on 11/9/17.
 */

public class FlightBookingReviewActivity extends BaseSimpleActivity implements HasComponent<FlightBookingComponent> {
    @Override
    protected Fragment getNewFragment() {
        return new FlightBookingReviewFragment();
    }

    @Override
    public FlightBookingComponent getComponent() {
        return DaggerFlightBookingComponent.builder()
                .flightComponent(((FlightModuleRouter) getApplication()).getFlightComponent())
                .build();
    }
}
