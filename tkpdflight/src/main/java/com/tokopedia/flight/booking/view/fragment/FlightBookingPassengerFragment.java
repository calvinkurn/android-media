package com.tokopedia.flight.booking.view.fragment;


import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.design.text.SpinnerTextView;
import com.tokopedia.design.text.TkpdHintTextInputLayout;
import com.tokopedia.flight.R;
import com.tokopedia.flight.booking.di.FlightBookingComponent;
import com.tokopedia.flight.booking.view.presenter.FlightBookingPassengerContract;
import com.tokopedia.flight.booking.view.presenter.FlightBookingPassengerPresenter;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingLuggageRouteViewModel;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingLuggageViewModel;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingMealRouteViewModel;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingMealViewModel;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingPassengerViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * A simple {@link Fragment} subclass.
 */
public class FlightBookingPassengerFragment extends BaseDaggerFragment implements FlightBookingPassengerContract.View {
    public static final String EXTRA_PASSENGER = "EXTRA_PASSENGER";
    public static final String EXTRA_LUGGAGES = "EXTRA_LUGGAGES";
    public static final String EXTRA_MEALS = "EXTRA_MEALS";
    public static final String EXTRA_DEPARTURE = "EXTRA_DEPARTURE";
    public static final String EXTRA_RETURN = "EXTRA_RETURN";

    private AppCompatTextView tvHeader;
    private AppCompatTextView tvSubheader;
    private SpinnerTextView spTitle;
    private TkpdHintTextInputLayout tilName;
    private AppCompatEditText etName;
    private TkpdHintTextInputLayout tilBirthDate;
    private AppCompatEditText etBirthDate;
    private LinearLayout luggageContainer;
    private RecyclerView rvLuggages;
    private LinearLayout mealsContainer;
    private RecyclerView rvMeals;
    private AppCompatButton buttonSubmit;

    private FlightBookingPassengerViewModel viewModel;
    private List<FlightBookingLuggageViewModel> luggageViewModels;
    private List<FlightBookingMealViewModel> mealViewModels;

    @Inject
    FlightBookingPassengerPresenter presenter;

    public static FlightBookingPassengerFragment newInstance(String departureId,
                                                             String returnId,
                                                             FlightBookingPassengerViewModel viewModel,
                                                             List<FlightBookingLuggageViewModel> luggageViewModels,
                                                             List<FlightBookingMealViewModel> mealViewModels) {
        FlightBookingPassengerFragment fragment = new FlightBookingPassengerFragment();
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_DEPARTURE, departureId);
        bundle.putString(EXTRA_RETURN, returnId);
        bundle.putParcelable(EXTRA_PASSENGER, viewModel);
        bundle.putParcelableArrayList(EXTRA_LUGGAGES, (ArrayList<? extends Parcelable>) luggageViewModels);
        bundle.putParcelableArrayList(EXTRA_MEALS, (ArrayList<? extends Parcelable>) mealViewModels);
        fragment.setArguments(bundle);
        return fragment;
    }


    public static Fragment newInstance(String departureId,
                                       FlightBookingPassengerViewModel viewModel,
                                       List<FlightBookingLuggageViewModel> luggageViewModels,
                                       List<FlightBookingMealViewModel> mealViewModels) {
        FlightBookingPassengerFragment fragment = new FlightBookingPassengerFragment();
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_DEPARTURE, departureId);
        bundle.putParcelable(EXTRA_PASSENGER, viewModel);
        bundle.putParcelableArrayList(EXTRA_LUGGAGES, (ArrayList<? extends Parcelable>) luggageViewModels);
        bundle.putParcelableArrayList(EXTRA_MEALS, (ArrayList<? extends Parcelable>) mealViewModels);
        fragment.setArguments(bundle);
        return fragment;
    }

    public FlightBookingPassengerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = getArguments().getParcelable(EXTRA_PASSENGER);
        luggageViewModels = getArguments().getParcelableArrayList(EXTRA_LUGGAGES);
        mealViewModels = getArguments().getParcelableArrayList(EXTRA_MEALS);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_flight_booking_passenger, container, false);

        tvHeader = (AppCompatTextView) view.findViewById(R.id.tv_header);
        tvSubheader = (AppCompatTextView) view.findViewById(R.id.tv_subheader);
        spTitle = (SpinnerTextView) view.findViewById(R.id.sp_title);
        tilName = (TkpdHintTextInputLayout) view.findViewById(R.id.til_name);
        etName = (AppCompatEditText) view.findViewById(R.id.et_name);
        tilBirthDate = (TkpdHintTextInputLayout) view.findViewById(R.id.til_birth_date);
        etBirthDate = (AppCompatEditText) view.findViewById(R.id.et_birth_date);
        luggageContainer = (LinearLayout) view.findViewById(R.id.luggage_container);
        rvLuggages = (RecyclerView) view.findViewById(R.id.rv_luggages);
        mealsContainer = (LinearLayout) view.findViewById(R.id.meals_container);
        rvMeals = (RecyclerView) view.findViewById(R.id.rv_meals);
        buttonSubmit = (AppCompatButton) view.findViewById(R.id.button_submit);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.attachView(this);
        presenter.onViewCreated();
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {
        getComponent(FlightBookingComponent.class).inject(this);
    }

    @Override
    public FlightBookingPassengerViewModel getCurrentPassengerViewModel() {
        return viewModel;
    }

    @Override
    public void renderSpinnerForAdult() {
        String[] entries = getResources().getStringArray(R.array.adult_spinner_titles);
        spTitle.setEntries(entries);
        spTitle.setValues(entries);
    }

    @Override
    public void renderSpinnerForChildAndInfant() {
        String[] entries = getResources().getStringArray(R.array.child_infant_spinner_titles);
        spTitle.setEntries(entries);
        spTitle.setValues(entries);
    }

    @Override
    public String getReturnTripId() {
        return getArguments().getString(EXTRA_RETURN);
    }

    @Override
    public String getDepartureId() {
        return getArguments().getString(EXTRA_DEPARTURE);
    }

    @Override
    public List<FlightBookingLuggageViewModel> getLuggageViewModels() {
        return luggageViewModels;
    }

    @Override
    public List<FlightBookingMealViewModel> getMealViewModels() {
        return mealViewModels;
    }

    @Override
    public void renderPassengerMeals(List<FlightBookingMealRouteViewModel> flightBookingMealRouteViewModels,
                                     List<FlightBookingMealRouteViewModel> selecteds) {

    }

    @Override
    public void renderPassengerLuggages(List<FlightBookingLuggageRouteViewModel> flightBookingLuggageRouteViewModels,
                                        List<FlightBookingLuggageRouteViewModel> selecteds) {

    }
}
