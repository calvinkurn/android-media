package com.tokopedia.flight.dashboard.view.activity;

import com.airbnb.deeplinkdispatch.DeepLink;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.di.component.HasComponent;
import com.tokopedia.abstraction.utils.Constants;
import com.tokopedia.flight.FlightModuleRouter;
import com.tokopedia.flight.dashboard.di.DaggerFlightDashboardComponent;
import com.tokopedia.flight.dashboard.di.FlightDashboardComponent;
import com.tokopedia.flight.dashboard.view.fragment.FlightDashboardFragment;

/**
 * Created by nathan on 10/19/17.
 */

public class FlightDashboardActivity extends BaseSimpleActivity implements HasComponent<FlightDashboardComponent> {

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
        if (getApplication() instanceof FlightModuleRouter) {
            return DaggerFlightDashboardComponent.builder()
                    .flightComponent(((FlightModuleRouter) getApplication()).getFlightComponent())
                    .build();
        }
        throw new RuntimeException("Application must implement FlightModuleRouter");
    }
}