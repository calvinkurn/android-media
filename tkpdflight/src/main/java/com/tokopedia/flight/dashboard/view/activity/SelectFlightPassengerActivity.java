package com.tokopedia.flight.dashboard.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.di.component.HasComponent;
import com.tokopedia.flight.FlightModuleRouter;
import com.tokopedia.flight.R;
import com.tokopedia.flight.dashboard.view.di.DaggerFlightDashboardComponent;
import com.tokopedia.flight.dashboard.view.di.FlightDashboardComponent;
import com.tokopedia.flight.dashboard.view.fragment.SelectFlightPassengerFragment;
import com.tokopedia.flight.dashboard.view.fragment.viewmodel.SelectFlightPassengerViewModel;

public class SelectFlightPassengerActivity extends BaseSimpleActivity implements HasComponent<FlightDashboardComponent>, SelectFlightPassengerFragment.OnFragmentInteractionListener {
    public static final String EXTRA_PASS_DATA = "EXTRA_PASS_DATA";

    public static Intent getCallingIntent(Activity activity, SelectFlightPassengerViewModel viewModel) {
        Intent intent = new Intent(activity, SelectFlightPassengerActivity.class);
        intent.putExtra(EXTRA_PASS_DATA, viewModel);
        return intent;
    }

    @Override
    protected Fragment getNewFragment() {
        SelectFlightPassengerViewModel passengerPassData = getIntent().getParcelableExtra(EXTRA_PASS_DATA);
        return SelectFlightPassengerFragment.newInstance(passengerPassData);
    }

    @Override
    protected boolean isToolbarWhite() {
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        updateTitle(getString(R.string.select_passenger_toolbar_title));
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
    public void actionSavePassenger(SelectFlightPassengerViewModel passData) {
        setIntent(getIntent().putExtra(EXTRA_PASS_DATA, passData));
        setResult(RESULT_OK, getIntent());
        finish();
    }
}
