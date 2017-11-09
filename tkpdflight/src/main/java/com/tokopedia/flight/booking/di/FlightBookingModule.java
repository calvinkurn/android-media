package com.tokopedia.flight.booking.di;

import com.tokopedia.flight.booking.domain.FlightBookingGetSingleResultUseCase;
import com.tokopedia.flight.common.domain.FlightRepository;

import dagger.Module;
import dagger.Provides;

/**
 * Created by alvarisi on 11/8/17.
 */
@Module
public class FlightBookingModule {
    @Provides
    FlightBookingGetSingleResultUseCase getSingleResultUseCase(FlightRepository flightRepository) {
        return new FlightBookingGetSingleResultUseCase(flightRepository);
    }
}
