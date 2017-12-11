package com.tokopedia.flight;

import android.content.Intent;

import com.tokopedia.flight.common.di.component.FlightComponent;

public interface FlightModuleRouter {

    FlightComponent getFlightComponent();

    long getLongConfig(String flightAirport);

    Intent getLoginIntent();
}