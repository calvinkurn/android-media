package com.tokopedia.flight.airport.di;

import com.tokopedia.flight.airport.data.source.FlightAirportDataListSource;
import com.tokopedia.flight.airport.domain.interactor.FlightAirportPickerUseCase;
import com.tokopedia.flight.airport.view.presenter.FlightAirportPickerPresenter;
import com.tokopedia.flight.airport.view.presenter.FlightAirportPickerPresenterImpl;
import com.tokopedia.flight.common.data.repository.FlightRepositoryImpl;
import com.tokopedia.flight.common.domain.FlightRepository;

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
    public FlightAirportPickerPresenter provideFlightAirportPickerPresenter(FlightAirportPickerUseCase flightAirportPickerUseCase){
        return new FlightAirportPickerPresenterImpl(flightAirportPickerUseCase);
    }

    @FlightAirportScope
    @Provides
    public FlightRepository provideFlightRepository(FlightAirportDataListSource flightAirportDataListSource){
        return new FlightRepositoryImpl(flightAirportDataListSource);
    }
}
