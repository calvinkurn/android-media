package com.tokopedia.flight.booking.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.di.component.HasComponent;
import com.tokopedia.flight.FlightComponentInstance;
import com.tokopedia.flight.FlightModuleRouter;
import com.tokopedia.flight.TkpdFlight;
import com.tokopedia.flight.booking.di.DaggerFlightBookingComponent;
import com.tokopedia.flight.booking.di.FlightBookingComponent;
import com.tokopedia.flight.booking.view.fragment.FlightBookingPassengerFragment;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingAmenityMetaViewModel;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingPassengerViewModel;

import java.util.ArrayList;
import java.util.List;

public class FlightBookingPassengerActivity extends BaseSimpleActivity implements HasComponent<FlightBookingComponent>, FlightBookingPassengerFragment.OnFragmentInteractionListener {
    public static final String EXTRA_PASSENGER = "EXTRA_PASSENGER";
    public static final String EXTRA_LUGGAGES = "EXTRA_LUGGAGES";
    public static final String EXTRA_MEALS = "EXTRA_MEALS";
    public static final String EXTRA_DEPARTURE = "EXTRA_DEPARTURE";
    public static final String EXTRA_RETURN = "EXTRA_RETURN";
    public static final String EXTRA_IS_AIRASIA = "EXTRA_IS_AIRASIA";
    private FlightBookingPassengerViewModel viewModel;

    public static Intent getCallingIntent(Activity activity,
                                          String departureId,
                                          String returnId,
                                          FlightBookingPassengerViewModel viewModel,
                                          List<FlightBookingAmenityMetaViewModel> luggageViewModels,
                                          List<FlightBookingAmenityMetaViewModel> mealViewModels,
                                          boolean isAirAsiaAirlines) {
        Intent intent = new Intent(activity, FlightBookingPassengerActivity.class);
        intent.putExtra(EXTRA_DEPARTURE, departureId);
        intent.putExtra(EXTRA_RETURN, returnId);
        intent.putExtra(EXTRA_PASSENGER, viewModel);
        intent.putParcelableArrayListExtra(EXTRA_LUGGAGES, (ArrayList<? extends Parcelable>) luggageViewModels);
        intent.putParcelableArrayListExtra(EXTRA_MEALS, (ArrayList<? extends Parcelable>) mealViewModels);
        intent.putExtra(EXTRA_IS_AIRASIA, isAirAsiaAirlines);
        return intent;
    }

    public static Intent getCallingIntent(Activity activity,
                                          String departureId,
                                          FlightBookingPassengerViewModel viewModel,
                                          List<FlightBookingAmenityMetaViewModel> luggageViewModels,
                                          List<FlightBookingAmenityMetaViewModel> mealViewModels) {
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

        List<FlightBookingAmenityMetaViewModel> luggageViewModels = getIntent().getParcelableArrayListExtra(EXTRA_LUGGAGES);
        List<FlightBookingAmenityMetaViewModel> mealViewModels = getIntent().getParcelableArrayListExtra(EXTRA_MEALS);
        if (getIntent().getStringExtra(EXTRA_RETURN) != null) {
            return FlightBookingPassengerFragment.newInstance(
                    getIntent().getStringExtra(EXTRA_DEPARTURE),
                    getIntent().getStringExtra(EXTRA_RETURN),
                    viewModel, luggageViewModels, mealViewModels,
                    getIntent().getBooleanExtra(EXTRA_IS_AIRASIA, false)
            );
        } else
            return FlightBookingPassengerFragment.newInstance(
                    getIntent().getStringExtra(EXTRA_DEPARTURE),
                    viewModel,
                    luggageViewModels,
                    mealViewModels,
                    getIntent().getBooleanExtra(EXTRA_IS_AIRASIA, false)
            );
    }

    @Override
    public FlightBookingComponent getComponent() {
        return DaggerFlightBookingComponent.builder()
                .flightComponent(FlightComponentInstance.getFlightComponent(getApplication()))
                .build();
    }

    @Override
    public void actionSuccessUpdatePassengerData(FlightBookingPassengerViewModel flightBookingPassengerViewModel) {
        Intent intent = getIntent();
        intent.putExtra(EXTRA_PASSENGER, flightBookingPassengerViewModel);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
}
