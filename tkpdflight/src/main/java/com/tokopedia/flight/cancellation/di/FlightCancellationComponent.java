package com.tokopedia.flight.cancellation.di;

import com.tokopedia.flight.common.di.component.FlightComponent;

import dagger.Component;

/**
 * @author by furqan on 21/03/18.
 */

@FlightCancellationScope
@Component(modules = FlightCancellationModule.class, dependencies = FlightComponent.class)
public interface FlightCancellationComponent {
}
