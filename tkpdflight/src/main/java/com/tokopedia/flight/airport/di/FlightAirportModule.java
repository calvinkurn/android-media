package com.tokopedia.flight.airport.di;

import com.tokopedia.flight.airport.domain.interactor.FlightAirportPickerUseCase;
import com.tokopedia.flight.airport.domain.interactor.FlightAirportVersionCheckUseCase;
import com.tokopedia.flight.airport.view.presenter.FlightAirportPickerPresenter;
import com.tokopedia.flight.airport.view.presenter.FlightAirportPickerPresenterImpl;

import dagger.Module;
import dagger.Provides;

/**
 * Created by zulfikarrahman on 10/24/17.
 */

@FlightAirportScope
@Module
public class FlightAirportModule {

    @FlightAirportScope
    @Provides
    public FlightAirportPickerPresenter provideFlightAirportPickerPresenter(FlightAirportPickerUseCase flightAirportPickerUseCase,
                                                                            FlightAirportVersionCheckUseCase flightAirportVersionCheckUseCase){
        return new FlightAirportPickerPresenterImpl(flightAirportPickerUseCase, flightAirportVersionCheckUseCase);
    }

}
