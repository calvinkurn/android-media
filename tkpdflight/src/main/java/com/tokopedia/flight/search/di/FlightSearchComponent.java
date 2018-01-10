package com.tokopedia.flight.search.di;

import com.tokopedia.flight.common.di.component.FlightComponent;
import com.tokopedia.flight.search.view.activity.FlightSearchFilterActivity;
import com.tokopedia.flight.search.view.fragment.FlightSearchFragment;
import com.tokopedia.flight.search.view.fragment.FlightSearchReturnFragment;

import dagger.Component;

/**
 * Created by zulfikarrahman on 10/24/17.
 */
@FlightSearchScope
@Component(modules = FlightSearchModule.class, dependencies = FlightComponent.class)
public interface FlightSearchComponent {

    void inject(FlightSearchFragment flightSearchFragment);

    void inject(FlightSearchReturnFragment flightSearchFragment);

    void inject(FlightSearchFilterActivity flightSearchFilterActivity);
}
