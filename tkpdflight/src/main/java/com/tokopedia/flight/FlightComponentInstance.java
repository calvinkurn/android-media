package com.tokopedia.flight;

import android.app.Application;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.flight.common.di.component.FlightComponent;
import com.tokopedia.flight.common.di.component.DaggerFlightComponent;

/**
 * Created by nakama on 11/12/17.
 */

public class FlightComponentInstance {
    private static FlightComponent flightComponent;

    public static FlightComponent getFlightComponent(Application application) {
        if (flightComponent == null) {
            flightComponent = DaggerFlightComponent.builder().baseAppComponent(
                    ((BaseMainApplication)application).getBaseAppComponent()).build();
        }
        return flightComponent;
    }
}
