package com.tokopedia.flight;

import android.content.Context;
import android.content.Intent;

import com.tokopedia.flight.common.di.component.FlightComponent;

public interface FlightModuleRouter {

    long getLongConfig(String flightAirport);

    Intent getLoginIntent();

    void goToFlightActivity(Context context);
}