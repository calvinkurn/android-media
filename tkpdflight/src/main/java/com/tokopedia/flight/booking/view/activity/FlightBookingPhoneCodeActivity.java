package com.tokopedia.flight.booking.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.di.component.HasComponent;
import com.tokopedia.flight.FlightModuleRouter;
import com.tokopedia.flight.booking.di.DaggerFlightBookingComponent;
import com.tokopedia.flight.booking.di.FlightBookingComponent;
import com.tokopedia.flight.booking.view.fragment.FLightBookingPhoneCodeFragment;

/**
 * Created by zulfikarrahman on 11/8/17.
 */

public class FlightBookingPhoneCodeActivity extends BaseSimpleActivity implements HasComponent<FlightBookingComponent> {

    public static Intent getCallingIntent(Activity activity) {
        return new Intent(activity, FlightBookingPhoneCodeActivity.class);
    }

    @Override
    protected Fragment getNewFragment() {
        return new FLightBookingPhoneCodeFragment();
    }

    @Override
    public FlightBookingComponent getComponent() {
        return DaggerFlightBookingComponent.builder()
                .flightComponent(((FlightModuleRouter) getApplication()).getFlightComponent())
                .build();
    }

    @Override
    protected boolean isToolbarWhite() {
        return true;
    }
}
