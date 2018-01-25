package com.tokopedia.flight.dashboard.view.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatDelegate;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.flight.FlightComponentInstance;
import com.tokopedia.flight.applink.ApplinkConstant;
import com.tokopedia.flight.common.util.FlightAnalytics;
import com.tokopedia.flight.common.view.BaseFlightActivity;
import com.tokopedia.flight.dashboard.di.DaggerFlightDashboardComponent;
import com.tokopedia.flight.dashboard.di.FlightDashboardComponent;
import com.tokopedia.flight.dashboard.view.fragment.FlightDashboardFragment;
import com.tokopedia.flight.dashboard.view.fragment.viewmodel.FlightDashboardViewModel;

/**
 * Created by nathan on 10/19/17.
 */

public class FlightDashboardActivity extends BaseFlightActivity implements HasComponent<FlightDashboardComponent> {

    private static final String EXTRA_DASHBOARD = "EXTRA_DASHBOARD";
    private static final String EXTRA_TRIP = "EXTRA_TRIP";
    private static final String EXTRA_PASSENGER = "EXTRA_PASSENGER";
    private static final String EXTRA_CLASS = "EXTRA_CLASS";

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, FlightDashboardActivity.class);
    }

    public static Intent getCallingIntent(Context context, FlightDashboardViewModel viewModel) {
        Intent intent = new Intent(context, FlightDashboardActivity.class);
        intent.putExtra(EXTRA_DASHBOARD, viewModel);
        return intent;
    }

    @DeepLink(ApplinkConstant.FLIGHT)
    public static Intent getCallingApplinkIntent(Context context, Bundle extras) {
        Uri.Builder uri = Uri.parse(extras.getString(DeepLink.URI)).buildUpon();
        Intent intent = new Intent(context, FlightDashboardActivity.class);
        return intent
                .setData(uri.build())
                .putExtras(extras);
    }

    @DeepLink(ApplinkConstant.FLIGHT_SEARCH)
    public static Intent getCallingApplinkSearchIntent(Context context, Bundle extras) {
        Uri.Builder uri = Uri.parse(extras.getString(DeepLink.URI)).buildUpon();
        Intent intent = new Intent(context, FlightDashboardActivity.class);
        intent.putExtra(EXTRA_TRIP, extras.getString("trip"));
        intent.putExtra(EXTRA_PASSENGER, extras.getString("passenger"));
        intent.putExtra(EXTRA_CLASS, extras.getString("class"));

        return intent
                .setData(uri.build());
    }

    @Override
    protected Fragment getNewFragment() {
        if (getIntent().hasExtra(EXTRA_TRIP) && getIntent().hasExtra(EXTRA_PASSENGER) && getIntent().hasExtra(EXTRA_CLASS)) {
            return FlightDashboardFragment.getInstance(
                getIntent().getStringExtra(EXTRA_TRIP),
                getIntent().getStringExtra(EXTRA_PASSENGER),
                getIntent().getStringExtra(EXTRA_CLASS)
            );
        } else {
            return FlightDashboardFragment.getInstance();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toolbar.setContentInsetStartWithNavigation(0);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    @Override
    public FlightDashboardComponent getComponent() {
        return DaggerFlightDashboardComponent.builder()
                .flightComponent(FlightComponentInstance.getFlightComponent(getApplication()))
                .build();
    }

    @Override
    public String getScreenName() {
        return FlightAnalytics.Screen.HOMEPAGE;
    }
}