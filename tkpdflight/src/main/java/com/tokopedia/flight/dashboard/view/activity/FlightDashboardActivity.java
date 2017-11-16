package com.tokopedia.flight.dashboard.view.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.di.component.HasComponent;
import com.tokopedia.flight.FlightModuleRouter;
import com.tokopedia.flight.dashboard.di.DaggerFlightDashboardComponent;
import com.tokopedia.flight.dashboard.di.FlightDashboardComponent;
import com.tokopedia.flight.dashboard.view.fragment.FlightDashboardFragment;

/**
 * Created by nathan on 10/19/17.
 */

public class FlightDashboardActivity extends BaseSimpleActivity implements HasComponent<FlightDashboardComponent> {

    @Override
    protected Fragment getNewFragment() {
        return FlightDashboardFragment.getInstance();
    }

    @Override
    protected boolean isToolbarWhite() {
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toolbar.setContentInsetStartWithNavigation(0);
    }

    @Override
    public FlightDashboardComponent getComponent() {
        if (getApplication() instanceof FlightModuleRouter) {
            return DaggerFlightDashboardComponent.builder()
                    .flightComponent(((FlightModuleRouter) getApplication()).getFlightComponent())
                    .build();
        }
        throw new RuntimeException("Application must implement FlightModuleRouter");
    }
}