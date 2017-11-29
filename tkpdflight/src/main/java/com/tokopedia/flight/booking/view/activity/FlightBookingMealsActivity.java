package com.tokopedia.flight.booking.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.flight.R;
import com.tokopedia.flight.booking.view.fragment.FlightBookingMealsFragment;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingAmenityMetaViewModel;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingAmenityViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zulfikarrahman on 11/8/17.
 */

public class FlightBookingMealsActivity extends BaseSimpleActivity {

    private FlightBookingAmenityMetaViewModel flightBookingAmenityMetaViewModel;

    public static Intent createIntent(Context context, List<FlightBookingAmenityViewModel> flightBookingAmenityViewModels,
                                      FlightBookingAmenityMetaViewModel selectedMeals) {
        Intent intent = new Intent(context, FlightBookingMealsActivity.class);
        intent.putParcelableArrayListExtra(FlightBookingMealsFragment.EXTRA_LIST_MEALS, (ArrayList<? extends Parcelable>) flightBookingAmenityViewModels);
        intent.putExtra(FlightBookingMealsFragment.EXTRA_SELECTED_MEALS, selectedMeals);
        return intent;
    }

    @Override
    protected Fragment getNewFragment() {
        flightBookingAmenityMetaViewModel = getIntent().getParcelableExtra(FlightBookingMealsFragment.EXTRA_SELECTED_MEALS);
        return FlightBookingMealsFragment
                .createInstance(
                        getIntent().<FlightBookingAmenityViewModel>getParcelableArrayListExtra(FlightBookingMealsFragment.EXTRA_LIST_MEALS),
                        flightBookingAmenityMetaViewModel
                );
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        updateTitle(String.format("%s %s", getString(R.string.flight_booking_meal_toolbar_title), flightBookingAmenityMetaViewModel.getDescription()));
    }
}
