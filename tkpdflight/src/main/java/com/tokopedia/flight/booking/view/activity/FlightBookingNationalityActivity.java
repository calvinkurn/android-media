package com.tokopedia.flight.booking.view.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.flight.FlightComponentInstance;
import com.tokopedia.flight.booking.di.DaggerFlightBookingComponent;
import com.tokopedia.flight.booking.di.FlightBookingComponent;
import com.tokopedia.flight.booking.view.fragment.FlightBookingNationalityFragment;

/**
 * Created by zulfikarrahman on 11/8/17.
 */

public class FlightBookingNationalityActivity extends BaseSimpleActivity implements HasComponent<FlightBookingComponent> {

    public static Intent createIntent(Context context) {
        Intent intent = new Intent(context, FlightBookingNationalityActivity.class);
        return intent;
    }

    @Override
    protected Fragment getNewFragment() {
        return new FlightBookingNationalityFragment();
    }

    @Override
    public FlightBookingComponent getComponent() {
        return DaggerFlightBookingComponent.builder()
                .flightComponent(FlightComponentInstance.getFlightComponent(getApplication()))
                .build();
    }
}
