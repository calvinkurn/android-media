package com.tokopedia.flight.orderlist.view;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.flight.FlightComponentInstance;
import com.tokopedia.flight.orderlist.di.DaggerFlightOrderComponent;
import com.tokopedia.flight.orderlist.di.FlightOrderComponent;

/**
 * Created by zulfikarrahman on 11/28/17.
 */

public class FlightOrderListActivity extends BaseSimpleActivity implements HasComponent<FlightOrderComponent> {
    FlightOrderComponent component;


    public static Intent getCallingIntent(Activity activity) {
        return new Intent(activity, FlightOrderListActivity.class);
    }

    @Override
    protected Fragment getNewFragment() {
        return FlightOrderListFragment.createInstance();
    }

    @Override
    public FlightOrderComponent getComponent() {
        if (component == null) {
            component = DaggerFlightOrderComponent.builder()
                    .flightComponent(FlightComponentInstance.getFlightComponent(getApplication()))
                    .build();
        }
        return component;
    }
}
