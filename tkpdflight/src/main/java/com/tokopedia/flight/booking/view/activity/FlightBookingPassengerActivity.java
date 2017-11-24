package com.tokopedia.flight.booking.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.di.component.HasComponent;
import com.tokopedia.flight.FlightModuleRouter;
import com.tokopedia.flight.booking.di.DaggerFlightBookingComponent;
import com.tokopedia.flight.booking.di.FlightBookingComponent;
import com.tokopedia.flight.booking.view.fragment.FlightBookingPassengerFragment;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingLuggageMetaViewModel;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingMealMetaViewModel;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingPassengerViewModel;

import java.util.ArrayList;
import java.util.List;

public class FlightBookingPassengerActivity extends BaseSimpleActivity implements HasComponent<FlightBookingComponent>, FlightBookingPassengerFragment.OnFragmentInteractionListener {
    public static final String EXTRA_PASSENGER = "EXTRA_PASSENGER";
    public static final String EXTRA_LUGGAGES = "EXTRA_LUGGAGES";
    public static final String EXTRA_MEALS = "EXTRA_MEALS";
    public static final String EXTRA_DEPARTURE = "EXTRA_DEPARTURE";
    public static final String EXTRA_RETURN = "EXTRA_RETURN";
    private FlightBookingPassengerViewModel viewModel;

    public static Intent getCallingIntent(Activity activity,
                                          String departureId,
                                          String returnId,
                                          FlightBookingPassengerViewModel viewModel,
                                          List<FlightBookingLuggageMetaViewModel> luggageViewModels,
                                          List<FlightBookingMealMetaViewModel> mealViewModels) {
        Intent intent = new Intent(activity, FlightBookingPassengerActivity.class);
        intent.putExtra(EXTRA_DEPARTURE, departureId);
        intent.putExtra(EXTRA_RETURN, returnId);
        intent.putExtra(EXTRA_PASSENGER, viewModel);
        intent.putParcelableArrayListExtra(EXTRA_LUGGAGES, (ArrayList<? extends Parcelable>) luggageViewModels);
        intent.putParcelableArrayListExtra(EXTRA_MEALS, (ArrayList<? extends Parcelable>) mealViewModels);
        return intent;
    }

    public static Intent getCallingIntent(Activity activity,
                                          String departureId,
                                          FlightBookingPassengerViewModel viewModel,
                                          List<FlightBookingLuggageMetaViewModel> luggageViewModels,
                                          List<FlightBookingMealMetaViewModel> mealViewModels) {
        Intent intent = new Intent(activity, FlightBookingPassengerActivity.class);
        intent.putExtra(EXTRA_DEPARTURE, departureId);
        intent.putExtra(EXTRA_PASSENGER, viewModel);
        intent.putParcelableArrayListExtra(EXTRA_LUGGAGES, (ArrayList<? extends Parcelable>) luggageViewModels);
        intent.putParcelableArrayListExtra(EXTRA_MEALS, (ArrayList<? extends Parcelable>) mealViewModels);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected Fragment getNewFragment() {
        viewModel = getIntent().getParcelableExtra(EXTRA_PASSENGER);

        List<FlightBookingLuggageMetaViewModel> luggageViewModels = getIntent().getParcelableArrayListExtra(EXTRA_LUGGAGES);
        List<FlightBookingMealMetaViewModel> mealViewModels = getIntent().getParcelableArrayListExtra(EXTRA_MEALS);
        if (getIntent().getStringExtra(EXTRA_RETURN) != null) {
            return FlightBookingPassengerFragment.newInstance(
                    getIntent().getStringExtra(EXTRA_DEPARTURE),
                    getIntent().getStringExtra(EXTRA_RETURN),
                    viewModel, luggageViewModels, mealViewModels
            );
        } else
            return FlightBookingPassengerFragment.newInstance(
                    getIntent().getStringExtra(EXTRA_DEPARTURE),
                    viewModel,
                    luggageViewModels,
                    mealViewModels
            );
    }

    @Override
    public FlightBookingComponent getComponent() {
        if (getApplication() instanceof FlightModuleRouter) {
            return DaggerFlightBookingComponent.builder()
                    .flightComponent(((FlightModuleRouter) getApplication()).getFlightComponent())
                    .build();
        }
        throw new RuntimeException("Application must implement FlightModuleRouter");
    }

    @Override
    public void actionSuccessUpdatePassengerData(FlightBookingPassengerViewModel flightBookingPassengerViewModel) {
        Intent intent = getIntent();
        intent.putExtra(EXTRA_PASSENGER, flightBookingPassengerViewModel);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
}
