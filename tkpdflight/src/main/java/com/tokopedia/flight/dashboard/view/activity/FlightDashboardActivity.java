package com.tokopedia.flight.dashboard.view.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.abstraction.di.component.HasComponent;
import com.tokopedia.abstraction.utils.Constants;
import com.tokopedia.flight.FlightComponentInstance;
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

    public static Intent getCallingIntent(Context context, FlightDashboardViewModel viewModel) {
        Intent intent = new Intent(context, FlightDashboardActivity.class);
        intent.putExtra(EXTRA_DASHBOARD, viewModel);
        return intent;
    }

    @DeepLink(Constants.Applinks.FLIGHT)
    public static Intent getCallingApplinkIntent(Context context, Bundle extras) {
        Uri.Builder uri = Uri.parse(extras.getString(DeepLink.URI)).buildUpon();
        Intent intent = new Intent(context, FlightDashboardActivity.class);
        return intent
                .setData(uri.build())
                .putExtras(extras);
    }

    @Override
    protected Fragment getNewFragment() {
        return FlightDashboardFragment.getInstance();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toolbar.setContentInsetStartWithNavigation(0);
    }

    @Override
    public FlightDashboardComponent getComponent() {
        return DaggerFlightDashboardComponent.builder()
                .flightComponent(FlightComponentInstance.getFlightComponent(getApplication()))
                .build();
    }
}