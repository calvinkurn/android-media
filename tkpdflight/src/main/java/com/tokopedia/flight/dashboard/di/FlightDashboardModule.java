package com.tokopedia.flight.dashboard.di;

import com.tokopedia.flight.common.domain.FlightRepository;
import com.tokopedia.flight.dashboard.domain.GetFlightClassesUseCase;

import dagger.Module;
import dagger.Provides;

/**
 * Created by alvarisi on 10/26/17.
 */
@Module
public class FlightDashboardModule {
    @Provides
    GetFlightClassesUseCase provideGetFlightClassesUseCase(FlightRepository flightRepository){
        return new GetFlightClassesUseCase(flightRepository);
    }
}
