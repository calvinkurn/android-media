package com.tokopedia.flight.cancellation.di;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.flight.FlightModuleRouter;
import com.tokopedia.flight.cancellation.domain.FlightCancellationUploadImageUseCase;

import dagger.Module;
import dagger.Provides;

/**
 * @author by furqan on 21/03/18.
 */

@Module
public class FlightCancellationModule {

    @Provides
    FlightCancellationUploadImageUseCase provideFlightCancellationUploadImageUseCase(FlightModuleRouter flightModuleRouter) {
        return new FlightCancellationUploadImageUseCase(flightModuleRouter);
    }

    @Provides
    FlightModuleRouter provideFlightModuleRouter(@ApplicationContext Context context) {
        if (context instanceof FlightModuleRouter) {
            return (FlightModuleRouter) context;
        }
        throw new RuntimeException("Application must implement FlightModuleRouter");
    }
}
