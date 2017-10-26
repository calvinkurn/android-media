package com.tokopedia.flight.airport.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.di.component.HasComponent;
import com.tokopedia.flight.FlightModuleRouter;
import com.tokopedia.flight.airport.view.fragment.FlightAirportPickerFragment;
import com.tokopedia.flight.common.di.component.FlightComponent;

/**
 * Created by nathan on 10/20/17.
 */

public class FlightAirportPickerActivity extends BaseSimpleActivity implements HasComponent<FlightComponent> {

    public static Intent createInstance(Activity activity){
        Intent intent = new Intent(activity, FlightAirportPickerActivity.class);
        return intent;
    }

    @Override
    protected Fragment getNewFragment() {
        return FlightAirportPickerFragment.getInstance();
    }

    @Override
    protected boolean isToolbarWhite() {
        return true;
    }

    @Override
    protected boolean isShowCloseButton() {
        return true;
    }

    @Override
    public FlightComponent getComponent() {
        if(getApplication() instanceof FlightModuleRouter) {
            return ((FlightModuleRouter)getApplication()).getFlightComponent();
        }
        return null;
    }
}