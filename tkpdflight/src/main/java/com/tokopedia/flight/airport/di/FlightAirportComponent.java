package com.tokopedia.flight.airport.di;

import com.tokopedia.flight.airport.service.GetAirportListService;
import com.tokopedia.flight.airport.view.fragment.FlightAirportPickerFragment;
import com.tokopedia.flight.common.di.component.FlightComponent;
import com.tokopedia.flight.common.di.scope.FlightScope;
import com.tokopedia.flight.common.domain.FlightRepository;

import dagger.Component;
import retrofit2.Retrofit;

/**
 * Created by zulfikarrahman on 10/24/17.
 */
@FlightAirportScope
@Component(modules = FlightAirportModule.class, dependencies = FlightComponent.class)
public interface FlightAirportComponent {
    void inject(FlightAirportPickerFragment flightAirportPickerFragment);

    void inject(GetAirportListService getAirportListService);
}
