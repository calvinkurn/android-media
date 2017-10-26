package com.tokopedia.flight.common.di.component;

import android.content.Context;

import com.tokopedia.abstraction.di.component.BaseAppComponent;
import com.tokopedia.abstraction.di.module.AppModule;
import com.tokopedia.abstraction.di.qualifier.ApplicationContext;
import com.tokopedia.flight.common.di.module.FlightModule;
import com.tokopedia.flight.common.di.scope.FlightScope;

import dagger.Component;

/**
 * @author sebastianuskh on 4/13/17.
 */
@FlightScope
@Component(modules = FlightModule.class, dependencies = BaseAppComponent.class)
public interface FlightComponent {
    @ApplicationContext
    Context context();
}