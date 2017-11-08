package com.tokopedia.flight.booking.view.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.flight.booking.view.fragment.FlightBookingLuggageFragment;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingLuggageViewModel;

import java.util.ArrayList;

/**
 * Created by zulfikarrahman on 11/7/17.
 */

public class FlightBookingLuggageActivity extends BaseSimpleActivity {

    public static Intent createIntent(Context context, ArrayList<FlightBookingLuggageViewModel> flightBookingLuggageViewModels,
                                      String selectedLuggage){
        Intent intent = new Intent(context, FlightBookingLuggageActivity.class);
        intent.putExtra(FlightBookingLuggageFragment.EXTRA_LIST_LUGGAGE, flightBookingLuggageViewModels);
        intent.putExtra(FlightBookingLuggageFragment.EXTRA_SELECTED_LUGGAGE, selectedLuggage);
        return intent;
    }

    @Override
    protected Fragment getNewFragment() {
        return FlightBookingLuggageFragment.createInstance(getIntent().<FlightBookingLuggageViewModel>getParcelableArrayListExtra(FlightBookingLuggageFragment.EXTRA_LIST_LUGGAGE),
                getIntent().getStringExtra(FlightBookingLuggageFragment.EXTRA_SELECTED_LUGGAGE));
    }
}
