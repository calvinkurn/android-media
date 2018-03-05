package com.tokopedia.flight.booking.view.fragment;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.LinearLayout;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.design.text.SpinnerTextView;
import com.tokopedia.design.text.TkpdHintTextInputLayout;
import com.tokopedia.flight.R;
import com.tokopedia.flight.booking.di.FlightBookingComponent;
import com.tokopedia.flight.booking.view.activity.FlightBookingAmenityActivity;
import com.tokopedia.flight.booking.view.activity.FlightBookingListPassengerActivity;
import com.tokopedia.flight.booking.view.adapter.FlightSimpleAdapter;
import com.tokopedia.flight.booking.view.presenter.FlightBookingPassengerContract;
import com.tokopedia.flight.booking.view.presenter.FlightBookingPassengerPresenter;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingAmenityMetaViewModel;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingAmenityViewModel;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingPassengerViewModel;
import com.tokopedia.flight.booking.view.viewmodel.SimpleViewModel;
import com.tokopedia.flight.common.util.FlightPassengerTitleType;

import java.util.ArrayList;
import java.util.Calendar;
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
    public static final String EXTRA_AIR_ASIA = "EXTRA_AIR_ASIA";
    public static final String EXTRA_DEPARTURE_DATE = "EXTRA_DEPARTURE_DATE";
    private static final int REQUEST_CODE_PICK_LUGGAGE = 1;
    private static final int REQUEST_CODE_PICK_MEAL = 2;
    private static final int REQUEST_CODE_PICK_SAVED_PASSENGER = 3;
    @Inject
    FlightBookingPassengerPresenter presenter;
    private AppCompatTextView tvHeader;
    private AppCompatTextView tvSubheader;
    private SpinnerTextView spTitle;
    private TkpdHintTextInputLayout tilFirstName;
    private AppCompatEditText etFirstName;
    private TkpdHintTextInputLayout tilLastName;
    private AppCompatEditText etLastName;
    private TkpdHintTextInputLayout tilBirthDate;
    private AppCompatEditText etBirthDate;
    private LinearLayout luggageContainer;
    private RecyclerView rvLuggages;
    private LinearLayout mealsContainer;
    private RecyclerView rvMeals;
    private AppCompatEditText etSavedPassenger;

    private AppCompatButton buttonSubmit;
    private FlightBookingPassengerViewModel viewModel;
    private List<FlightBookingAmenityMetaViewModel> luggageViewModels;
    private List<FlightBookingAmenityMetaViewModel> mealViewModels;

    private OnFragmentInteractionListener interactionListener;

    private boolean isAirAsiaAirlines = false;
    private String departureDate;

    public FlightBookingPassengerFragment() {
        // Required empty public constructor
    }

    public static FlightBookingPassengerFragment newInstance(String departureId,
                                                             String returnId,
                                                             FlightBookingPassengerViewModel viewModel,
                                                             List<FlightBookingAmenityMetaViewModel> luggageViewModels,
                                                             List<FlightBookingAmenityMetaViewModel> mealViewModels,
                                                             boolean isAirAsiaAirlines,
                                                             String departureDate) {
        FlightBookingPassengerFragment fragment = new FlightBookingPassengerFragment();
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_DEPARTURE, departureId);
        bundle.putString(EXTRA_RETURN, returnId);
        bundle.putString(EXTRA_DEPARTURE_DATE, departureDate);
        bundle.putBoolean(EXTRA_AIR_ASIA, isAirAsiaAirlines);
        bundle.putParcelable(EXTRA_PASSENGER, viewModel);
        bundle.putParcelableArrayList(EXTRA_LUGGAGES, (ArrayList<? extends Parcelable>) luggageViewModels);
        bundle.putParcelableArrayList(EXTRA_MEALS, (ArrayList<? extends Parcelable>) mealViewModels);
        fragment.setArguments(bundle);
        return fragment;
    }


    public static Fragment newInstance(String departureId,
                                       FlightBookingPassengerViewModel viewModel,
                                       List<FlightBookingAmenityMetaViewModel> luggageViewModels,
                                       List<FlightBookingAmenityMetaViewModel> mealViewModels,
                                       boolean isAirAsiaAirlines,
                                       String departureDate) {
        FlightBookingPassengerFragment fragment = new FlightBookingPassengerFragment();
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_DEPARTURE, departureId);
        bundle.putString(EXTRA_DEPARTURE_DATE, departureDate);
        bundle.putBoolean(EXTRA_AIR_ASIA, isAirAsiaAirlines);
        bundle.putParcelable(EXTRA_PASSENGER, viewModel);
        bundle.putParcelableArrayList(EXTRA_LUGGAGES, (ArrayList<? extends Parcelable>) luggageViewModels);
        bundle.putParcelableArrayList(EXTRA_MEALS, (ArrayList<? extends Parcelable>) mealViewModels);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = getArguments().getParcelable(EXTRA_PASSENGER);
        luggageViewModels = getArguments().getParcelableArrayList(EXTRA_LUGGAGES);
        mealViewModels = getArguments().getParcelableArrayList(EXTRA_MEALS);
        isAirAsiaAirlines = getArguments().getBoolean(EXTRA_AIR_ASIA);
        departureDate = getArguments().getString(EXTRA_DEPARTURE_DATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_flight_booking_passenger, container, false);

        tvHeader = (AppCompatTextView) view.findViewById(R.id.tv_header);
        tvSubheader = (AppCompatTextView) view.findViewById(R.id.tv_subheader);
        spTitle = (SpinnerTextView) view.findViewById(R.id.sp_title);
        tilFirstName = (TkpdHintTextInputLayout) view.findViewById(R.id.til_first_name);
        etFirstName = (AppCompatEditText) view.findViewById(R.id.et_first_name);
        tilLastName = (TkpdHintTextInputLayout) view.findViewById(R.id.til_last_name);
        etLastName = (AppCompatEditText) view.findViewById(R.id.et_last_name);
        tilBirthDate = (TkpdHintTextInputLayout) view.findViewById(R.id.til_birth_date);
        etBirthDate = (AppCompatEditText) view.findViewById(R.id.et_birth_date);
        luggageContainer = (LinearLayout) view.findViewById(R.id.luggage_container);
        rvLuggages = (RecyclerView) view.findViewById(R.id.rv_luggages);
        mealsContainer = (LinearLayout) view.findViewById(R.id.meals_container);
        rvMeals = (RecyclerView) view.findViewById(R.id.rv_meals);
        etSavedPassenger = view.findViewById(R.id.et_saved_passenger);
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
        etSavedPassenger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onSavedPassengerClicked();
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
    public void setCurrentPassengerViewModel(FlightBookingPassengerViewModel flightBookingPassengerViewModel) {
        viewModel = flightBookingPassengerViewModel;
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
    public List<FlightBookingAmenityMetaViewModel> getLuggageViewModels() {
        return luggageViewModels;
    }

    @Override
    public List<FlightBookingAmenityMetaViewModel> getMealViewModels() {
        return mealViewModels;
    }

    @Override
    public void renderPassengerMeals(final List<FlightBookingAmenityMetaViewModel> flightBookingMealRouteViewModels,
                                     List<FlightBookingAmenityMetaViewModel> selecteds) {
        mealsContainer.setVisibility(View.VISIBLE);

        List<SimpleViewModel> viewModels = new ArrayList<>();
        if (flightBookingMealRouteViewModels != null)
            for (FlightBookingAmenityMetaViewModel flightBookingAmenityMetaViewModel : flightBookingMealRouteViewModels) {
                SimpleViewModel viewModel = new SimpleViewModel(
                        flightBookingAmenityMetaViewModel.getDescription(),
                        getString(R.string.flight_booking_passenger_choose_label)
                );
                for (FlightBookingAmenityMetaViewModel selected : selecteds) {
                    if (selected.getKey().equalsIgnoreCase(flightBookingAmenityMetaViewModel.getKey())) {
                        ArrayList<String> selectedMeals = new ArrayList<>();
                        for (FlightBookingAmenityViewModel flightBookingAmenityViewModel : selected.getAmenities()) {
                            selectedMeals.add(flightBookingAmenityViewModel.getTitle());
                        }
                        viewModel.setDescription(TextUtils.join(",", selectedMeals));
                        break;
                    }
                }
                viewModels.add(viewModel);
            }
        FlightSimpleAdapter adapter = new FlightSimpleAdapter();
        adapter.setArrowVisible(true);
        adapter.setFontSize(getResources().getDimension(R.dimen.font_micro));
        adapter.setInteractionListener(new FlightSimpleAdapter.OnAdapterInteractionListener() {
            @Override
            public void onItemClick(int adapterPosition, SimpleViewModel viewModel) {
                presenter.onOptionMeal(flightBookingMealRouteViewModels.get(adapterPosition));
            }
        });
        LinearLayoutManager flightSimpleAdapterLayoutManager
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvMeals.setLayoutManager(flightSimpleAdapterLayoutManager);
        rvMeals.setHasFixedSize(true);
        rvMeals.setNestedScrollingEnabled(false);
        rvMeals.setAdapter(adapter);
        adapter.setDescriptionTextColor(getResources().getColor(R.color.colorPrimary));
        adapter.setViewModels(viewModels);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void renderPassengerLuggages(final List<FlightBookingAmenityMetaViewModel> flightBookingLuggageRouteViewModels,
                                        List<FlightBookingAmenityMetaViewModel> selecteds) {
        luggageContainer.setVisibility(View.VISIBLE);

        List<SimpleViewModel> viewModels = new ArrayList<>();
        if (flightBookingLuggageRouteViewModels != null)
            for (FlightBookingAmenityMetaViewModel flightBookingLuggageMetaViewModel : flightBookingLuggageRouteViewModels) {
                SimpleViewModel viewModel = new SimpleViewModel(
                        flightBookingLuggageMetaViewModel.getDescription(),
                        getString(R.string.flight_booking_passenger_choose_label)
                );
                for (FlightBookingAmenityMetaViewModel selected : selecteds) {
                    if (selected.getKey().equalsIgnoreCase(flightBookingLuggageMetaViewModel.getKey())) {
                        ArrayList<String> selectedLuggages = new ArrayList<>();
                        for (FlightBookingAmenityViewModel flightBookingLuggageViewModel : selected.getAmenities()) {
                            selectedLuggages.add(flightBookingLuggageViewModel.getTitle() + " - " + flightBookingLuggageViewModel.getPrice());
                        }
                        viewModel.setDescription(TextUtils.join(",", selectedLuggages));
                        break;
                    }
                }
                viewModels.add(viewModel);
            }
        FlightSimpleAdapter adapter = new FlightSimpleAdapter();
        adapter.setArrowVisible(true);
        adapter.setFontSize(getResources().getDimension(R.dimen.font_micro));
        adapter.setInteractionListener(new FlightSimpleAdapter.OnAdapterInteractionListener() {
            @Override
            public void onItemClick(int adapterPosition, SimpleViewModel viewModel) {
                presenter.onPassengerLuggageClick(flightBookingLuggageRouteViewModels.get(adapterPosition));
            }
        });
        LinearLayoutManager flightSimpleAdapterLayoutManager
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvLuggages.setLayoutManager(flightSimpleAdapterLayoutManager);
        rvLuggages.setHasFixedSize(true);
        rvLuggages.setNestedScrollingEnabled(false);
        rvLuggages.setAdapter(adapter);
        adapter.setDescriptionTextColor(getResources().getColor(R.color.colorPrimary));
        adapter.setViewModels(viewModels);
        adapter.notifyDataSetChanged();
    }

    @Override
    public int getPassengerTitleId() {
        switch (spTitle.getSpinnerPosition()) {
            case 0:
                return FlightPassengerTitleType.TUAN;
            case 1:
                return FlightPassengerTitleType.NYONYA;
            case 2:
                return FlightPassengerTitleType.NONA;
            default:
                return 0;
        }
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
    public String getPassengerFirstName() {
        return etFirstName.getText().toString().trim();
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
    public void showPassengerFirstNameShouldNoMoreThanMaxError(int resId) {
        showMessageErrorInSnackBar(resId);
    }

    @Override
    public void showPassengerLastNameEmptyError(int resId) {
        showMessageErrorInSnackBar(resId);
    }

    @Override
    public void showPassengerLastNameShouldNoMoreThanMaxError(int resId) {
        showMessageErrorInSnackBar(resId);
    }

    @Override
    public void showPassengerLastNameShouldOneWordError(int resId) {
        showMessageErrorInSnackBar(resId);
    }

    @Override
    public void showPassengerFirstNameShouldAlphabetAndSpaceOnlyError(int resId) {
        showMessageErrorInSnackBar(resId);
    }

    @Override
    public void showPassengerLastNameShouldAlphabetAndSpaceOnlyError(int resId) {
        showMessageErrorInSnackBar(resId);
    }


    @Override
    public void showPassengerLastNameShouldSameWithFirstNameError(int resId) {
        showMessageErrorInSnackBar(resId);
    }

    @Override
    public void hideKeyboard() {
        KeyboardHandler.hideSoftKeyboard(getActivity());
    }

    @Override
    public boolean isAirAsiaAirline() {
        return isAirAsiaAirlines;
    }

    @Override
    public String getDepartureDateString() {
        return departureDate;
    }

    @Override
    public String getPassengerLastName() {
        return etLastName.getText().toString().trim();
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
        NetworkErrorHelper.showRedCloseSnackbar(getActivity(), getString(resId));
    }

    @Override
    public void showBirthdatePickerDialog(Date selectedDate, final Date minDate, final Date maxDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(selectedDate);
        DatePickerDialog datePicker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                presenter.onBirthdateChange(year, month, dayOfMonth, minDate, maxDate);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));
        DatePicker datePicker1 = datePicker.getDatePicker();
        datePicker1.setMinDate(minDate.getTime());
        datePicker1.setMaxDate(maxDate.getTime());
        datePicker.show();
    }

    @Override
    public void showBirthdatePickerDialog(Date selectedDate, final Date maxDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(selectedDate);
        DatePickerDialog datePicker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                presenter.onBirthdateChange(year, month, dayOfMonth, maxDate);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));
        DatePicker datePicker1 = datePicker.getDatePicker();
        datePicker1.setMaxDate(maxDate.getTime());
        datePicker.show();
    }

    @Override
    public void renderBirthdate(String birthdateStr) {
        etBirthDate.setText(birthdateStr);
    }

    @Override
    public void renderPassengerName(String passengerName, String passengerLastName) {
        etFirstName.setText(passengerName);
        etLastName.setText(passengerLastName);
    }

    @Override
    public void renderPassengerTitle(String passengerTitle) {
        spTitle.setSpinnerValueByEntries(passengerTitle);
    }

    @Override
    public void renderSelectedList(String passengerName) {
        etSavedPassenger.setText(passengerName);
    }

    @Override
    public void navigateToLuggagePicker(List<FlightBookingAmenityViewModel> luggages, FlightBookingAmenityMetaViewModel selected) {
        String title = String.format("%s %s", getString(R.string.flight_booking_luggage_toolbar_title), selected.getDescription());
        Intent intent = FlightBookingAmenityActivity.createIntent(getActivity(), title, luggages, selected);
        startActivityForResult(intent, REQUEST_CODE_PICK_LUGGAGE);
    }

    @Override
    public void navigateToMealPicker(List<FlightBookingAmenityViewModel> viewModel, FlightBookingAmenityMetaViewModel selected) {
        String title = String.format("%s %s", getString(R.string.flight_booking_meal_toolbar_title), selected.getDescription());
        Intent intent = FlightBookingAmenityActivity.createIntent(getActivity(), title, viewModel, selected);
        startActivityForResult(intent, REQUEST_CODE_PICK_MEAL);
    }

    @Override
    public void navigateToSavedPassengerPicker(FlightBookingPassengerViewModel selected) {
        Intent intent = FlightBookingListPassengerActivity.createIntent(getActivity(), selected);
        startActivityForResult(intent, REQUEST_CODE_PICK_SAVED_PASSENGER);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_PICK_LUGGAGE:
                    if (data != null) {
                        FlightBookingAmenityMetaViewModel flightBookingLuggageMetaViewModel = data.getParcelableExtra(FlightBookingAmenityFragment.EXTRA_SELECTED_AMENITIES);
                        presenter.onLuggageDataChange(flightBookingLuggageMetaViewModel);
                    }
                    break;
                case REQUEST_CODE_PICK_MEAL:
                    if (data != null) {
                        FlightBookingAmenityMetaViewModel flightBookingLuggageMetaViewModel = data.getParcelableExtra(FlightBookingAmenityFragment.EXTRA_SELECTED_AMENITIES);
                        presenter.onMealDataChange(flightBookingLuggageMetaViewModel);
                    }
                    break;
                case REQUEST_CODE_PICK_SAVED_PASSENGER:
                    if (data != null) {
                        FlightBookingPassengerViewModel flightBookingPassengerViewModel = data.getParcelableExtra(FlightBookingListPassengerFragment.EXTRA_SELECTED_PASSENGER);
                        presenter.onChangeFromSavedPassenger(flightBookingPassengerViewModel);
                    } else {
                        etSavedPassenger.setText(getString(R.string.flight_booking_passenger_saved_secondary_hint));
                        viewModel.setPassengerId("");
                    }
                    break;
            }
        }
    }

    public interface OnFragmentInteractionListener {
        void actionSuccessUpdatePassengerData(FlightBookingPassengerViewModel flightBookingPassengerViewModel);
    }
}
