package com.tokopedia.flight.review.view.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.flight.FlightComponentInstance;
import com.tokopedia.flight.booking.di.DaggerFlightBookingComponent;
import com.tokopedia.flight.booking.di.FlightBookingComponent;
import com.tokopedia.flight.common.util.FlightAnalytics;
import com.tokopedia.flight.common.view.BaseFlightActivity;
import com.tokopedia.flight.review.view.fragment.FlightBookingReviewFragment;
import com.tokopedia.flight.review.view.model.FlightBookingReviewModel;

/**
 * Created by zulfikarrahman on 11/9/17.
 */

public class FlightBookingReviewActivity extends BaseFlightActivity implements HasComponent<FlightBookingComponent> {

    public static Intent createIntent(Context context, FlightBookingReviewModel flightBookingReviewModel){
        Intent intent = new Intent(context, FlightBookingReviewActivity.class);
        intent.putExtra(FlightBookingReviewFragment.EXTRA_DATA_REVIEW, flightBookingReviewModel);
        return intent;
    }

    @Override
    protected Fragment getNewFragment() {
        return FlightBookingReviewFragment.createInstance((FlightBookingReviewModel) getIntent().getParcelableExtra(FlightBookingReviewFragment.EXTRA_DATA_REVIEW));
    }

    @Override
    public FlightBookingComponent getComponent() {
        return DaggerFlightBookingComponent.builder()
                .flightComponent(FlightComponentInstance.getFlightComponent(getApplication()))
                .build();
    }

    @Override
    public void onBackPressed() {
        if (getFragment() instanceof OnBackActionListener) {
            if (!((OnBackActionListener) getFragment()).isCanGoBack()) {
                ((OnBackActionListener) getFragment()).onBackPressed();
                return;
            }
        }
        super.onBackPressed();
    }

    @Override
    public String getScreenName() {
        return FlightAnalytics.Screen.REVIEW;
    }
}
