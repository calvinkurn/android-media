package com.tokopedia.flight.passenger.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.flight.FlightComponentInstance;
import com.tokopedia.flight.passenger.di.DaggerFlightPassengerComponent;
import com.tokopedia.flight.passenger.di.FlightPassengerComponent;
import com.tokopedia.flight.passenger.view.fragment.FlightPassengerUpdateFragment;

public class FlightPassengerUpdateActivity extends BaseSimpleActivity
        implements HasComponent<FlightPassengerComponent> {

    public static final String EXTRA_PASSENGER_ID = "EXTRA_PASSENGER_ID";

    public static Intent getCallingIntent(Activity activity,
                                          String passengerId) {
        Intent intent = new Intent(activity, FlightPassengerUpdateActivity.class);
        intent.putExtra(EXTRA_PASSENGER_ID, passengerId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected Fragment getNewFragment() {
        return FlightPassengerUpdateFragment.newInstance(
                getIntent().getExtras().getString(EXTRA_PASSENGER_ID)
        );
    }

    @Override
    public FlightPassengerComponent getComponent() {
        return DaggerFlightPassengerComponent.builder()
                .flightComponent(FlightComponentInstance.getFlightComponent(getApplication()))
                .build();
    }
}
