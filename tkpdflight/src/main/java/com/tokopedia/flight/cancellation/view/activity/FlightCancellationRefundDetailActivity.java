package com.tokopedia.flight.cancellation.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.flight.FlightModuleRouter;
import com.tokopedia.flight.cancellation.di.DaggerFlightCancellationComponent;
import com.tokopedia.flight.cancellation.di.FlightCancellationComponent;
import com.tokopedia.flight.cancellation.view.fragment.FlightCancellationRefundDetailFragment;
import com.tokopedia.flight.cancellation.view.viewmodel.FlightCancellationWrapperViewModel;
import com.tokopedia.flight.common.view.BaseFlightActivity;

/**
 * Created by alvarisi on 4/9/18.
 */

public class FlightCancellationRefundDetailActivity extends BaseFlightActivity implements HasComponent<FlightCancellationComponent> {
    private static final String FLIWGHT_WRAPPER_EXTRA = "FLIWGHT_WRAPPER_EXTRA";
    private FlightCancellationComponent cancellationComponent;

    private FlightCancellationWrapperViewModel wrapperViewModel;

    private static Intent getCallingIntent(Activity activity, FlightCancellationWrapperViewModel wrapperViewModel) {
        Intent intent = new Intent(activity, FlightCancellationRefundDetailActivity.class);
        intent.putExtra(FLIWGHT_WRAPPER_EXTRA, wrapperViewModel);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        wrapperViewModel = getIntent().getParcelableExtra(FLIWGHT_WRAPPER_EXTRA);
        super.onCreate(savedInstanceState);

    }

    @Override
    public FlightCancellationComponent getComponent() {
        if (cancellationComponent == null) {
            initInjector();
        }
        return cancellationComponent;
    }

    @Override
    protected Fragment getNewFragment() {
        return FlightCancellationRefundDetailFragment.newInstance(wrapperViewModel);
    }

    private void initInjector() {
        if (getApplication() instanceof FlightModuleRouter) {
            cancellationComponent = DaggerFlightCancellationComponent.builder()
                    .flightComponent(getFlightComponent())
                    .build();
        } else {
            throw new RuntimeException("Application must implement FlightModuleRouter");
        }
    }
}
