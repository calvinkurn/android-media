package com.tokopedia.flight.cancellation.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.flight.FlightModuleRouter;
import com.tokopedia.flight.R;
import com.tokopedia.flight.cancellation.di.DaggerFlightCancellationComponent;
import com.tokopedia.flight.cancellation.di.FlightCancellationComponent;
import com.tokopedia.flight.cancellation.view.fragment.FlightCancellationFragment;
import com.tokopedia.flight.cancellation.view.fragment.FlightReviewCancellationFragment;
import com.tokopedia.flight.cancellation.view.viewmodel.FlightCancellationJourney;
import com.tokopedia.flight.cancellation.view.viewmodel.FlightCancellationViewModel;
import com.tokopedia.flight.common.view.BaseFlightActivity;

import java.util.ArrayList;
import java.util.List;

import static com.tokopedia.flight.cancellation.view.fragment.FlightReviewCancellationFragment.EXTRA_CANCEL_JOURNEY;
import static com.tokopedia.flight.cancellation.view.fragment.FlightReviewCancellationFragment.EXTRA_INVOICE_ID;

public class FlightReviewCancellationActivity extends BaseFlightActivity implements HasComponent<FlightCancellationComponent> {

    public static Intent createIntent(Context context, String invoiceId,
                                      List<FlightCancellationViewModel> flightCancellationPassData) {
        Intent intent = new Intent(context, FlightReviewCancellationActivity.class);
        intent.putExtra(EXTRA_INVOICE_ID, invoiceId);
        intent.putParcelableArrayListExtra(EXTRA_CANCEL_JOURNEY, (ArrayList<? extends Parcelable>) flightCancellationPassData);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupToolbar();
    }

    private void setupToolbar() {
        toolbar.setContentInsetStartWithNavigation(0);
        toolbar.setSubtitleTextColor(ContextCompat.getColor(this, R.color.grey_500));
        String title = getString(R.string.activity_label_flight_review_cancellation);
        String subtitle = String.format(
                getString(R.string.flight_cancellation_subtitle_order_id),
                getIntent().getExtras().getString(FlightCancellationFragment.EXTRA_INVOICE_ID)
        );
        updateTitle(title, subtitle);
    }

    @Override
    public FlightCancellationComponent getComponent() {
        if (getApplication() instanceof FlightModuleRouter) {
            return DaggerFlightCancellationComponent.builder()
                    .flightComponent(getFlightComponent())
                    .build();
        }
        throw new RuntimeException("Application must implement FlightModuleRouter");
    }

    @Override
    protected Fragment getNewFragment() {
        List<FlightCancellationJourney> flightCancellationJourneyList = getIntent().getExtras()
                .getParcelableArrayList(EXTRA_CANCEL_JOURNEY);
        return FlightReviewCancellationFragment.createInstance(
                getIntent().getExtras().getString(EXTRA_INVOICE_ID),
                flightCancellationJourneyList
        );
    }
}
