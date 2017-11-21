package com.tokopedia.flight.booking.view.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.flight.booking.view.fragment.FlightBookingMealsFragment;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingMealViewModel;

import java.util.ArrayList;

/**
 * Created by zulfikarrahman on 11/8/17.
 */

public class FlightBookingMealsActivity extends BaseSimpleActivity {

    public static Intent createIntent(Context context, ArrayList<FlightBookingMealViewModel> flightBookingMealViewModels,
                                      ArrayList<String> selectedMeals){
        Intent intent = new Intent(context, FlightBookingMealsActivity.class);
        intent.putParcelableArrayListExtra(FlightBookingMealsFragment.EXTRA_LIST_MEALS, flightBookingMealViewModels);
        intent.putStringArrayListExtra(FlightBookingMealsFragment.EXTRA_SELECTED_MEALS, selectedMeals);
        return intent;
    }

    @Override
    protected Fragment getNewFragment() {
        return FlightBookingMealsFragment.createInstance(getIntent().<FlightBookingMealViewModel>getParcelableArrayListExtra(FlightBookingMealsFragment.EXTRA_LIST_MEALS),
                getIntent().getStringArrayListExtra(FlightBookingMealsFragment.EXTRA_SELECTED_MEALS));
    }
}
