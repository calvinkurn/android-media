package com.tokopedia.flight.booking.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuItem;

import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.flight.FlightModuleRouter;
import com.tokopedia.flight.R;
import com.tokopedia.flight.booking.di.DaggerFlightBookingComponent;
import com.tokopedia.flight.booking.di.FlightBookingComponent;
import com.tokopedia.flight.booking.view.adapter.viewholder.FlightBookingListPassengerViewHolder;
import com.tokopedia.flight.booking.view.fragment.FlightBookingListPassengerFragment;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingPassengerViewModel;
import com.tokopedia.flight.common.view.BaseFlightActivity;

import java.util.List;

/**
 * @author by furqan on 23/02/18.
 */

public class FlightBookingListPassengerActivity extends BaseFlightActivity implements HasComponent<FlightBookingComponent> {

    public static Intent createIntent(Context context, FlightBookingPassengerViewModel selected) {
        Intent intent = new Intent(context, FlightBookingListPassengerActivity.class);
        intent.putExtra(FlightBookingListPassengerFragment.EXTRA_SELECTED_PASSENGER, selected);
        return intent;
    }
    @Override
    protected Fragment getNewFragment() {
        FlightBookingPassengerViewModel flightBookingPassengerViewModel = getIntent()
                .getParcelableExtra(FlightBookingListPassengerFragment.EXTRA_SELECTED_PASSENGER);
        return FlightBookingListPassengerFragment.createInstance(flightBookingPassengerViewModel);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        updateTitle(getString(R.string.pilih_penumpang_title));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_flight_amenity_info_reset, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_reset) {
            onResetClicked();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void onResetClicked() {
        Fragment f = getCurrentFragment();
        if (f != null && f instanceof FlightBookingListPassengerViewHolder.ListenerCheckedSavedPassenger) {
            ((FlightBookingListPassengerViewHolder.ListenerCheckedSavedPassenger) f).resetItemCheck();
        }
    }

    private Fragment getCurrentFragment() {
        List<Fragment> fragmentList = getSupportFragmentManager().getFragments();
        for (int i = 0, sizei = fragmentList.size(); i < sizei; i++) {
            Fragment fragment = fragmentList.get(i);
            if (fragment.isAdded() && fragment.isVisible()) {
                return fragment;
            }
        }
        return null;
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
