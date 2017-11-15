package com.tokopedia.flight.common.di.component;

import android.content.Context;

import com.google.gson.Gson;
import com.tokopedia.abstraction.di.component.BaseAppComponent;
import com.tokopedia.abstraction.di.qualifier.ApplicationContext;
import com.tokopedia.flight.common.di.module.FlightModule;
import com.tokopedia.flight.common.di.qualifier.FlightQualifier;
import com.tokopedia.flight.common.di.scope.FlightScope;
import com.tokopedia.flight.common.domain.FlightRepository;

import dagger.Component;
import retrofit2.Retrofit;

/**
 * @author sebastianuskh on 4/13/17.
 */
@FlightScope
@Component(modules = FlightModule.class,  dependencies = BaseAppComponent.class)
public interface FlightComponent {
    @ApplicationContext
    Context context();

    @FlightQualifier
    Retrofit getFlightRetrofit();

    FlightRepository flightRepository();

    Gson gson();
}