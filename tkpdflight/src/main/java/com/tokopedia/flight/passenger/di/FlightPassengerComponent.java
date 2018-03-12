package com.tokopedia.flight.passenger.di;

import com.tokopedia.flight.common.di.component.FlightComponent;
import com.tokopedia.flight.passenger.view.fragment.FlightPassengerUpdateFragment;

import dagger.Component;

/**
 * @author by furqan on 12/03/18.
 */

@FlightPassengerScope
@Component(modules = FlightPassengerModule.class, dependencies = FlightComponent.class)
public interface FlightPassengerComponent {

    void inject(FlightPassengerUpdateFragment flightPassengerUpdateFragment);

}
