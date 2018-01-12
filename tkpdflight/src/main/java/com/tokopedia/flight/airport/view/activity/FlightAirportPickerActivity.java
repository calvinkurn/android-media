package com.tokopedia.flight.airport.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.flight.FlightComponentInstance;
import com.tokopedia.flight.airport.view.fragment.FlightAirportPickerFragment;
import com.tokopedia.flight.common.di.component.FlightComponent;

/**
 * Created by nathan on 10/20/17.
 */

public class FlightAirportPickerActivity extends BaseSimpleActivity implements HasComponent<FlightComponent> {
    private static final String EXTRA_TOOLBAR_TITLE = "EXTRA_TOOLBAR_TITLE";

    public static Intent createInstance(Activity activity, String title) {
        Intent intent = new Intent(activity, FlightAirportPickerActivity.class);
        intent.putExtra(EXTRA_TOOLBAR_TITLE, title);
        return intent;
    }

    @Override
    protected Fragment getNewFragment() {
        return FlightAirportPickerFragment.getInstance();
    }

    @Override
    protected boolean isShowCloseButton() {
        return true;
    }

    @Override
    public FlightComponent getComponent() {
        return FlightComponentInstance.getFlightComponent(getApplication());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        updateTitle(getIntent().getStringExtra(EXTRA_TOOLBAR_TITLE));
    }
}