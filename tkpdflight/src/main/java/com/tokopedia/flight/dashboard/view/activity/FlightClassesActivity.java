package com.tokopedia.flight.dashboard.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.di.component.HasComponent;
import com.tokopedia.flight.FlightModuleRouter;
import com.tokopedia.flight.dashboard.di.DaggerFlightDashboardComponent;
import com.tokopedia.flight.dashboard.di.FlightDashboardComponent;
import com.tokopedia.flight.dashboard.view.fragment.FlightClassesfragment;
import com.tokopedia.flight.dashboard.view.fragment.viewmodel.FlightClassViewModel;

/**
 * Created by alvarisi on 10/30/17.
 */

public class FlightClassesActivity extends BaseSimpleActivity implements HasComponent<FlightDashboardComponent>,
        FlightClassesfragment.OnFragmentInteractionListener {
    public static final String EXTRA_FLIGHT_CLASS = "EXTRA_FLIGHT_CLASS";

    public static Intent getCallingIntent(Activity activity) {
        return new Intent(activity, FlightClassesActivity.class);
    }

    @Override
    protected Fragment getNewFragment() {
        return FlightClassesfragment.newInstance();
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

    @Override
    public void actionClassSelected(FlightClassViewModel flightClassViewModel) {
        setIntent(getIntent().putExtra(EXTRA_FLIGHT_CLASS, flightClassViewModel));
        setResult(RESULT_OK, getIntent());
        finish();
    }
}
