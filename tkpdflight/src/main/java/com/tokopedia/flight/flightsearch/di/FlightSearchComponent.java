package com.tokopedia.flight.flightsearch.di;

import com.tokopedia.flight.common.di.component.FlightComponent;
import com.tokopedia.flight.flightsearch.view.fragment.FlightSearchFragment;

import dagger.Component;

/**
 * Created by zulfikarrahman on 10/24/17.
 */
@FlightSearchScope
@Component(modules = FlightSearchModule.class, dependencies = FlightComponent.class)
public interface FlightSearchComponent {
    void inject(FlightSearchFragment flightSearchFragment);
}
