package com.tokopedia.flight.airport.di;

import android.content.Context;

import com.tokopedia.abstraction.base.data.source.cache.DataListCacheSource;
import com.tokopedia.abstraction.base.data.source.cloud.DataListCloudSource;
import com.tokopedia.abstraction.di.qualifier.ApplicationContext;
import com.tokopedia.flight.airport.data.source.FlightAirportDataListSource;
import com.tokopedia.flight.airport.data.source.cache.FlightAirportDataListCacheSource;
import com.tokopedia.flight.airport.data.source.cloud.FlightAirportDataListCloudSource;
import com.tokopedia.flight.airport.data.source.cloud.model.FlightAirportCountry;
import com.tokopedia.flight.airport.data.source.cloud.service.FlightAirportService;
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

    @FlightAirportScope
    @Provides
    public DataListCacheSource dataListCacheSource(@ApplicationContext Context context){
        return new FlightAirportDataListCacheSource(context);
    }

    @FlightAirportScope
    @Provides
    public DataListCloudSource<FlightAirportCountry> dataListCloudSource(FlightAirportService flightAirportService){
        return new FlightAirportDataListCloudSource(flightAirportService);
    }

    @FlightAirportScope
    @Provides
    public FlightAirportService flightAirportService(){
        return new FlightAirportService();
    }
}
