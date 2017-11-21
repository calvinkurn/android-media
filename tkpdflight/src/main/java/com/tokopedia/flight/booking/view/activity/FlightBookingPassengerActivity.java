package com.tokopedia.flight.booking.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.flight.booking.view.fragment.FlightBookingPassengerFragment;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingPassengerViewModel;

public class FlightBookingPassengerActivity extends BaseSimpleActivity {
    public static final String EXTRA_PASSENGER = "EXTRA_PASSENGER";
    private FlightBookingPassengerViewModel viewModel;

    public static Intent getCallingIntent(Activity activity, FlightBookingPassengerViewModel viewModel) {
        Intent intent = new Intent(activity, FlightBookingPassengerActivity.class);
        intent.putExtra(EXTRA_PASSENGER, viewModel);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = getIntent().getParcelableExtra(EXTRA_PASSENGER);
    }

    @Override
    protected Fragment getNewFragment() {
        return FlightBookingPassengerFragment.newInstance(viewModel);
    }
}
