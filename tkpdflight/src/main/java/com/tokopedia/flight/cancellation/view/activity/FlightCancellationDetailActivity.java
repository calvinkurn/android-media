package com.tokopedia.flight.cancellation.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.flight.FlightModuleRouter;
import com.tokopedia.flight.R;
import com.tokopedia.flight.cancellation.di.DaggerFlightCancellationComponent;
import com.tokopedia.flight.cancellation.di.FlightCancellationComponent;
import com.tokopedia.flight.cancellation.view.viewmodel.FlightCancellationListViewModel;
import com.tokopedia.flight.common.view.BaseFlightActivity;

import static com.tokopedia.flight.cancellation.view.fragment.FlightCancellationDetailFragment.EXTRA_CANCELLATION_DETAIL_PASS_DATA;

public class FlightCancellationDetailActivity extends BaseFlightActivity implements HasComponent<FlightCancellationComponent> {

    public static Intent createIntent(Context context,
                                      FlightCancellationListViewModel cancellationListViewModel) {
        Intent intent = new Intent(context, FlightCancellationDetailActivity.class);
        intent.putExtra(EXTRA_CANCELLATION_DETAIL_PASS_DATA, cancellationListViewModel);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupToolbar();
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
        return null;
    }

    private void setupToolbar() {
        FlightCancellationListViewModel flightCancellationListViewModel = getIntent()
                .getExtras().getParcelable(EXTRA_CANCELLATION_DETAIL_PASS_DATA);

        toolbar.setContentInsetStartWithNavigation(0);
        toolbar.setSubtitleTextColor(ContextCompat.getColor(this, R.color.grey_500));
        String title = getString(R.string.flight_cancellation_list_title);
        String subtitle = String.format(
                getString(R.string.flight_cancellation_list_id),
                Long.toString(flightCancellationListViewModel.getCancellations().getRefundId())
        );
        updateTitle(title, subtitle);
    }
}
