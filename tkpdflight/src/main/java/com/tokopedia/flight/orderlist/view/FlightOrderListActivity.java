package com.tokopedia.flight.orderlist.view;

import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.di.component.HasComponent;
import com.tokopedia.flight.FlightModuleRouter;
import com.tokopedia.flight.orderlist.di.DaggerFlightOrderComponent;
import com.tokopedia.flight.orderlist.di.FlightOrderComponent;

/**
 * Created by zulfikarrahman on 11/28/17.
 */

public class FlightOrderListActivity extends BaseSimpleActivity implements HasComponent<FlightOrderComponent> {
    FlightOrderComponent component;

    @Override
    protected Fragment getNewFragment() {
        return FlightOrderListFragment.createInstance();
    }

    @Override
    public FlightOrderComponent getComponent() {
        if (component == null) {
            if (getApplication() instanceof FlightModuleRouter) {
                component = DaggerFlightOrderComponent.builder()
                        .flightComponent(((FlightModuleRouter) getApplication()).getFlightComponent())
                        .build();
            } else {
                throw new RuntimeException("Application must implement FlightModuleRouter");
            }
        }
        return component;
    }
}
