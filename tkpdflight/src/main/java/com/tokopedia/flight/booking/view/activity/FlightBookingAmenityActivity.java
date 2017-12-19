package com.tokopedia.flight.booking.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.flight.booking.view.fragment.FlightBookingAmenityFragment;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingAmenityMetaViewModel;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingAmenityViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zulfikarrahman on 11/7/17.
 */

public class FlightBookingAmenityActivity extends BaseSimpleActivity {
    private static final String EXTRA_TITLE = "EXTRA_TITLE";

    public static Intent createIntent(Context context, String title, List<FlightBookingAmenityViewModel> flightBookingAmenityViewModels,
                                      FlightBookingAmenityMetaViewModel selectedLuggage) {
        Intent intent = new Intent(context, FlightBookingAmenityActivity.class);
        intent.putParcelableArrayListExtra(FlightBookingAmenityFragment.EXTRA_LIST_AMENITIES, (ArrayList<? extends Parcelable>) flightBookingAmenityViewModels);
        intent.putExtra(FlightBookingAmenityFragment.EXTRA_SELECTED_AMENITIES, selectedLuggage);
        intent.putExtra(EXTRA_TITLE, title);
        return intent;
    }

    @Override
    protected Fragment getNewFragment() {
        FlightBookingAmenityMetaViewModel metaViewModel = getIntent().getParcelableExtra(FlightBookingAmenityFragment.EXTRA_SELECTED_AMENITIES);
        return FlightBookingAmenityFragment.createInstance(getIntent().<FlightBookingAmenityMetaViewModel>getParcelableArrayListExtra(FlightBookingAmenityFragment.EXTRA_LIST_AMENITIES),
                metaViewModel);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        updateTitle(getIntent().getStringExtra(EXTRA_TITLE));
    }
}
