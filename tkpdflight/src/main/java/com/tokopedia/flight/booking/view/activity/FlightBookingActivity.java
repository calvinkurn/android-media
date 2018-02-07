package com.tokopedia.flight.booking.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.flight.FlightModuleRouter;
import com.tokopedia.flight.booking.di.DaggerFlightBookingComponent;
import com.tokopedia.flight.booking.di.FlightBookingComponent;
import com.tokopedia.flight.booking.view.fragment.FlightBookingFragment;
import com.tokopedia.flight.common.util.FlightAnalytics;
import com.tokopedia.flight.common.view.BaseFlightActivity;
import com.tokopedia.flight.search.view.model.FlightSearchPassDataViewModel;

/**
 * Created by alvarisi on 11/6/17.
 */

public class FlightBookingActivity extends BaseFlightActivity implements HasComponent<FlightBookingComponent> {
    private static final String EXTRA_PASS_SEARCH_DATA = "EXTRA_PASS_SEARCH_DATA";
    private static final String EXTRA_FLIGHT_DEPARTURE_ID = "EXTRA_FLIGHT_DEPARTURE_ID";
    private static final String EXTRA_FLIGHT_ARRIVAL_ID = "EXTRA_FLIGHT_ARRIVAL_ID";

    public static Intent getCallingIntent(Activity activity, FlightSearchPassDataViewModel passDataViewModel, String departureId) {
        Intent intent = new Intent(activity, FlightBookingActivity.class);
        intent.putExtra(EXTRA_FLIGHT_DEPARTURE_ID, departureId);
        intent.putExtra(EXTRA_PASS_SEARCH_DATA, passDataViewModel);
        return intent;
    }

    public static Intent getCallingIntent(Activity activity, FlightSearchPassDataViewModel passDataViewModel, String departureId, String returnId) {
        Intent intent = new Intent(activity, FlightBookingActivity.class);
        intent.putExtra(EXTRA_FLIGHT_DEPARTURE_ID, departureId);
        intent.putExtra(EXTRA_FLIGHT_ARRIVAL_ID, returnId);
        intent.putExtra(EXTRA_PASS_SEARCH_DATA, passDataViewModel);
        return intent;
    }

    @Override
    public String getScreenName() {
        return FlightAnalytics.Screen.BOOKING;
    }

    @Override
    protected Fragment getNewFragment() {
        String departureId = getIntent().getStringExtra(EXTRA_FLIGHT_DEPARTURE_ID);
        String arrivalId = getIntent().getStringExtra(EXTRA_FLIGHT_ARRIVAL_ID);
        FlightSearchPassDataViewModel searchPassDataViewModel = getIntent().getParcelableExtra(EXTRA_PASS_SEARCH_DATA);
        return FlightBookingFragment.newInstance(searchPassDataViewModel, departureId, arrivalId);
    }

    @Override
    public FlightBookingComponent getComponent() {
        if (getApplication() instanceof FlightModuleRouter) {
            return DaggerFlightBookingComponent.builder()
                    .flightComponent(getFlightComponent())
                    .build();
        }
        throw new RuntimeException("Application must implement FlightModuleRouter");
    }
}
