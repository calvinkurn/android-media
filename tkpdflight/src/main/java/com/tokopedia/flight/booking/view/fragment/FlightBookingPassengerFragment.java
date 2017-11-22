package com.tokopedia.flight.booking.view.fragment;


import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.utils.snackbar.SnackbarManager;
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
import java.util.Date;
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

    public interface OnFragmentInteractionListener {
        void actionSuccessUpdatePassengerData(FlightBookingPassengerViewModel flightBookingPassengerViewModel);
    }

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

    private OnFragmentInteractionListener interactionListener;

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
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onSaveButtonClicked();
            }
        });
        etBirthDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onBirthdateClicked();
            }
        });
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

    @Override
    public void hideBirthdayInputView() {
        tilBirthDate.setVisibility(View.GONE);
    }

    @Override
    public void showBirthdayInputView() {
        tilBirthDate.setVisibility(View.VISIBLE);
    }

    @Override
    public void renderHeaderTitle(String headerTitle) {
        tvHeader.setText(headerTitle);
    }

    @Override
    public void renderHeaderSubtitle(int resId) {
        tvSubheader.setText(getString(resId));
    }

    @Override
    public String getPassengerName() {
        return etName.getText().toString().trim();
    }

    @Override
    public void showPassengerNameEmptyError(int resId) {
        showMessageErrorInSnackBar(resId);
    }

    @Override
    public String getPassengerTitle() {
        return spTitle.getSpinnerValue().equalsIgnoreCase(String.valueOf(SpinnerTextView.DEFAULT_INDEX_NOT_SELECTED))
                ? "" : spTitle.getSpinnerValue();
    }

    @Override
    public void showPassengerTitleEmptyError(int resId) {
        showMessageErrorInSnackBar(resId);
    }

    @Override
    public String getPassengerBirthDate() {
        return etBirthDate.getText().toString().trim();
    }

    @Override
    public void showPassengerBirthdateEmptyError(int resId) {
        showMessageErrorInSnackBar(resId);
    }

    @Override
    public void showPassengerChildBirthdateShouldMoreThan2Years(int resId) {
        showMessageErrorInSnackBar(resId);
    }

    @Override
    public void showPassengerInfantBirthdateShouldNoMoreThan2Years(int resID) {
        showMessageErrorInSnackBar(resID);
    }

    @Override
    public void navigateResultUpdatePassengerData(FlightBookingPassengerViewModel currentPassengerViewModel) {
        if (interactionListener != null) {
            interactionListener.actionSuccessUpdatePassengerData(currentPassengerViewModel);
        }
    }

    @Override
    protected void onAttachActivity(Context context) {
        if (context instanceof OnFragmentInteractionListener) {
            interactionListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException("Activity must implement OnFragmentInteractionListener");
        }
    }

    @SuppressWarnings("Range")
    private void showMessageErrorInSnackBar(int resId) {
        Snackbar snackBar = SnackbarManager.make(getActivity(),
                getString(resId), Snackbar.LENGTH_LONG)
                .setAction("Tutup", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
        Button snackBarAction = (Button) snackBar.getView().findViewById(android.support.design.R.id.snackbar_action);
        snackBarAction.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
        snackBar.getView().setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.red_500));
        snackBar.show();
    }

    @Override
    public void showBirthdatePickerDialog(Date selectedDate, Date minDate, Date maxDate) {
        DatePickerDialog datePicker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                presenter.onBirthdateChange(year, month, dayOfMonth);
            }
        }, selectedDate.getYear(), selectedDate.getMonth(), selectedDate.getDay());
        DatePicker datePicker1 = datePicker.getDatePicker();
        datePicker1.setMinDate(minDate.getTime());
        datePicker1.setMaxDate(maxDate.getTime());
        datePicker.show();
    }

    @Override
    public void renderBirthdate(String birthdateStr) {
        etBirthDate.setText(birthdateStr);
    }

    @Override
    public void renderPassengerName(String passengerName) {
        etName.setText(passengerName);
    }

    @Override
    public void renderPassengerTitle(String passengerTitle) {
        spTitle.setSpinnerValueByEntries(passengerTitle);
    }
}
