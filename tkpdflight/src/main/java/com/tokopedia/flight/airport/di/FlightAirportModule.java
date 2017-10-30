package com.tokopedia.flight.airport.di;

import android.content.Context;

import com.tokopedia.abstraction.base.data.source.cache.DataListCacheSource;
import com.tokopedia.abstraction.base.data.source.cloud.DataListCloudSource;
import com.tokopedia.abstraction.di.qualifier.ApplicationContext;
import com.tokopedia.flight.airport.data.source.FlightAirportDataListSource;
import com.tokopedia.flight.airport.data.source.cache.FlightAirportDataListCacheSource;
import com.tokopedia.flight.airport.data.source.cloud.FlightAirportDataListCloudSource;
import com.tokopedia.flight.airport.data.source.cloud.api.FlightAirportApi;
import com.tokopedia.flight.airport.data.source.cloud.model.FlightAirportCountry;
import com.tokopedia.flight.airport.domain.interactor.FlightAirportPickerUseCase;
import com.tokopedia.flight.airport.view.presenter.FlightAirportPickerPresenter;
import com.tokopedia.flight.airport.view.presenter.FlightAirportPickerPresenterImpl;
import com.tokopedia.flight.common.data.repository.FlightRepositoryImpl;
import com.tokopedia.flight.common.di.module.FlightModule;
import com.tokopedia.flight.common.di.qualifier.FlightQualifier;
import com.tokopedia.flight.common.domain.FlightRepository;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

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

}
