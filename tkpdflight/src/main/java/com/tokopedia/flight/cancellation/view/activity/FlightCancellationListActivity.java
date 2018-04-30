package com.tokopedia.flight.cancellation.view.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.flight.FlightModuleRouter;
import com.tokopedia.flight.cancellation.di.DaggerFlightCancellationComponent;
import com.tokopedia.flight.cancellation.di.FlightCancellationComponent;
import com.tokopedia.flight.common.view.BaseFlightActivity;

public class FlightCancellationListActivity extends BaseFlightActivity implements HasComponent<FlightCancellationComponent> {

    public static Intent createIntent(Context context) {
        Intent intent = new Intent(context, FlightCancellationListActivity.class);

        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public FlightCancellationComponent getComponent() {
        if (getApplication() instanceof FlightModuleRouter) {
            return DaggerFlightCancellationComponent.builder()
                    .flightComponent(getFlightComponent())
                    .build();
        }
        throw new RuntimeException("Application must implement FlightModuleRouter");
    }

    @Override
    protected Fragment getNewFragment() {
        return null;
    }
}
