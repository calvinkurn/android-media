package com.tokopedia.flight.common.di.component;

import android.content.Context;

import com.google.gson.Gson;
import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.flight.common.di.module.FlightModule;
import com.tokopedia.flight.common.di.qualifier.FlightQualifier;
import com.tokopedia.flight.common.di.scope.FlightScope;
import com.tokopedia.flight.common.domain.FlightRepository;
import com.tokopedia.flight.common.view.BaseFlightActivity;
import com.tokopedia.flight.detail.view.activity.FlightDetailActivity;

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

    UserSession userSession();

    AnalyticTracker analyticTracker();

    void inject(BaseFlightActivity baseFlightActivity);

    void inject(FlightDetailActivity flightDetailActivity);
}