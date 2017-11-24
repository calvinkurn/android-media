package com.tokopedia.flight.booking.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.flight.R;
import com.tokopedia.flight.booking.view.fragment.FlightBookingLuggageFragment;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingLuggageMetaViewModel;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingLuggageViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zulfikarrahman on 11/7/17.
 */

public class FlightBookingLuggageActivity extends BaseSimpleActivity {

    private FlightBookingLuggageMetaViewModel metaViewModel;

    public static Intent createIntent(Context context, List<FlightBookingLuggageViewModel> flightBookingLuggageViewModels,
                                      FlightBookingLuggageMetaViewModel selectedLuggage) {
        Intent intent = new Intent(context, FlightBookingLuggageActivity.class);
        intent.putParcelableArrayListExtra(FlightBookingLuggageFragment.EXTRA_LIST_LUGGAGE, (ArrayList<? extends Parcelable>) flightBookingLuggageViewModels);
        intent.putExtra(FlightBookingLuggageFragment.EXTRA_SELECTED_LUGGAGE, selectedLuggage);
        return intent;
    }

    @Override
    protected Fragment getNewFragment() {
        metaViewModel = getIntent().getParcelableExtra(FlightBookingLuggageFragment.EXTRA_SELECTED_LUGGAGE);
        return FlightBookingLuggageFragment.createInstance(getIntent().<FlightBookingLuggageViewModel>getParcelableArrayListExtra(FlightBookingLuggageFragment.EXTRA_LIST_LUGGAGE),
                metaViewModel);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        updateTitle(String.format("%s %s", getString(R.string.flight_booking_luggage_toolbar_title), metaViewModel.getDescription()));
    }
}
