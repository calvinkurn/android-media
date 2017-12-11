package com.tokopedia.flight;

import com.tokopedia.flight.common.di.component.FlightComponent;

public interface FlightModuleRouter {

    FlightComponent getFlightComponent();

    long getLongConfig(String flightAirport);
}