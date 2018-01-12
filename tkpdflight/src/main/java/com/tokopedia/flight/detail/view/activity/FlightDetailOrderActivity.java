package com.tokopedia.flight.detail.view.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.flight.FlightComponentInstance;
import com.tokopedia.flight.detail.view.fragment.FlightDetailOrderFragment;
import com.tokopedia.flight.orderlist.di.FlightOrderComponent;
import com.tokopedia.flight.orderlist.view.viewmodel.FlightOrderDetailPassData;
import com.tokopedia.flight.orderlist.di.DaggerFlightOrderComponent;

/**
 * Created by zulfikarrahman on 12/12/17.
 */

public class FlightDetailOrderActivity extends BaseSimpleActivity implements HasComponent<FlightOrderComponent> {

    public static final String EXTRA_ORDER_PASS_DETAIL = "EXTRA_ORDER_PASS_DETAIL";

    public static Intent createIntent(Context context, FlightOrderDetailPassData flightOrderDetailPassData) {
        Intent intent = new Intent(context, FlightDetailOrderActivity.class);
        intent.putExtra(EXTRA_ORDER_PASS_DETAIL, flightOrderDetailPassData);
        return intent;
    }

    @Override
    protected Fragment getNewFragment() {
        return FlightDetailOrderFragment.createInstance((FlightOrderDetailPassData) getIntent().getParcelableExtra(EXTRA_ORDER_PASS_DETAIL));
    }

    @Override
    public FlightOrderComponent getComponent() {
        return DaggerFlightOrderComponent.builder()
                .flightComponent(FlightComponentInstance.getFlightComponent(getApplication()))
                .build();
    }
}
