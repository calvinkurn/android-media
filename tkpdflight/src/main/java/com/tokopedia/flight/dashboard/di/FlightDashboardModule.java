package com.tokopedia.flight.dashboard.di;

import android.content.Context;

import com.google.gson.Gson;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.flight.common.domain.FlightRepository;
import com.tokopedia.flight.dashboard.domain.GetFlightClassesUseCase;
import com.tokopedia.flight.dashboard.view.fragment.cache.FlightDashboardCache;

import dagger.Module;
import dagger.Provides;

/**
 * Created by alvarisi on 10/26/17.
 */
@Module
public class FlightDashboardModule {

    public FlightDashboardModule() {
    }

    @Provides
    GetFlightClassesUseCase provideGetFlightClassesUseCase(FlightRepository flightRepository) {
        return new GetFlightClassesUseCase(flightRepository);
    }

    @Provides
    FlightDashboardCache provideFlightDashboardCache(@ApplicationContext Context context, Gson gson) {
        return new FlightDashboardCache(context, gson);
    }
}
