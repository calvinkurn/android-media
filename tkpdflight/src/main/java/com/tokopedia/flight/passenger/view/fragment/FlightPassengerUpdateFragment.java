package com.tokopedia.flight.passenger.view.fragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.design.text.SpinnerTextView;
import com.tokopedia.flight.R;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingPassengerViewModel;
import com.tokopedia.flight.passenger.di.FlightPassengerComponent;
import com.tokopedia.flight.passenger.view.presenter.FlightPassengerUpdateContract;
import com.tokopedia.flight.passenger.view.presenter.FlightPassengerUpdatePresenter;

import java.util.Calendar;
import java.util.Date;

import javax.inject.Inject;

/**
 * @author by furqan on 12/03/18.
 */

public class FlightPassengerUpdateFragment extends BaseDaggerFragment implements FlightPassengerUpdateContract.View {

    public static final String EXTRA_PASSENGER_VIEW_MODEL = "EXTRA_PASSENGER_VIEW_MODEL";
    public static final String EXTRA_DEPARTURE_DATE = "EXTRA_DEPARTURE_DATE";
    public static final String EXTRA_REQUEST_ID = "EXTRA_REQUEST_ID";

    @Inject
    FlightPassengerUpdatePresenter presenter;
    private FlightBookingPassengerViewModel flightBookingPassengerViewModel;

    private SpinnerTextView spPassengerTitle;
    private AppCompatEditText etPassengerType;
    private AppCompatEditText etPassengerFirstName;
    private AppCompatEditText etPassengerLastName;
    private AppCompatEditText etPassengerBirthdate;
    private AppCompatButton btnSavePassengerInfo;

    public FlightPassengerUpdateFragment() {
    }

    public static FlightPassengerUpdateFragment newInstance(FlightBookingPassengerViewModel passengerViewModel,
                                                            String departureDate, String requestId) {
        FlightPassengerUpdateFragment flightPassengerUpdateFragment = new FlightPassengerUpdateFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_PASSENGER_VIEW_MODEL, passengerViewModel);
        bundle.putString(EXTRA_DEPARTURE_DATE, departureDate);
        bundle.putString(EXTRA_REQUEST_ID, requestId);
        flightPassengerUpdateFragment.setArguments(bundle);
        return flightPassengerUpdateFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_flight_passenger_update, container, false);
        spPassengerTitle = view.findViewById(R.id.sp_title);
        etPassengerType = view.findViewById(R.id.et_passenger_type);
        etPassengerFirstName = view.findViewById(R.id.et_first_name);
        etPassengerLastName = view.findViewById(R.id.et_last_name);
        etPassengerBirthdate = view.findViewById(R.id.et_birth_date);
        btnSavePassengerInfo = view.findViewById(R.id.button_submit);

        etPassengerBirthdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onBirthdateClicked();
            }
        });
        btnSavePassengerInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onSaveButtonClicked();
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        flightBookingPassengerViewModel = getArguments().getParcelable(EXTRA_PASSENGER_VIEW_MODEL);

        presenter.attachView(this);
        presenter.onViewCreated();
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {
        getComponent(FlightPassengerComponent.class).inject(this);
    }

    @Override
    public String getDepartureDate() {
        if (getArguments() != null) {
            return getArguments().getString(EXTRA_DEPARTURE_DATE);
        }
        return "";
    }

    @Override
    public FlightBookingPassengerViewModel getCurrentPassengerViewModel() {
        return flightBookingPassengerViewModel;
    }

    @Override
    public String getPassengerBirthdate() {
        return etPassengerBirthdate.getText().toString().trim();
    }

    @Override
    public String getPassengerFirstName() {
        return etPassengerFirstName.getText().toString().trim();
    }

    @Override
    public String getPassengerLastName() {
        return etPassengerLastName.getText().toString().trim();
    }

    @Override
    public String getPassengerTitle() {
        return spPassengerTitle.getSpinnerValue().equalsIgnoreCase(
                String.valueOf(SpinnerTextView.DEFAULT_INDEX_NOT_SELECTED)) ? "" :
                spPassengerTitle.getSpinnerValue();
    }

    @Override
    public int getPassengerTitlePosition() {
        return spPassengerTitle.getSpinnerPosition();
    }

    @Override
    public void setPassengerViewModel(FlightBookingPassengerViewModel flightBookingPassengerViewModel) {
        this.flightBookingPassengerViewModel = flightBookingPassengerViewModel;
    }

    @Override
    public void renderSpinnerForAdult() {
        String[] entries = getResources().getStringArray(R.array.adult_spinner_titles);
        spPassengerTitle.setEntries(entries);
        spPassengerTitle.setValues(entries);
    }

    @Override
    public void renderSpinnerForChildAndInfant() {
        String[] entries = getResources().getStringArray(R.array.child_infant_spinner_titles);
        spPassengerTitle.setEntries(entries);
        spPassengerTitle.setValues(entries);
    }

    @Override
    public void renderSelectedTitle(String passengerTitle) {
        spPassengerTitle.setSpinnerValueByEntries(passengerTitle);
    }

    @Override
    public void renderPassengerType(String typeText) {
        etPassengerType.setText(typeText);
    }

    @Override
    public void renderPassengerName(String firstName, String lastName) {
        etPassengerFirstName.setText(firstName);
        etPassengerLastName.setText(lastName);
    }

    @Override
    public void renderPassengerBirthdate(String birthdate) {
        etPassengerBirthdate.setText(birthdate);
    }

    @Override
    public void showBirthdatePickerDialog(Date selectedDate, final Date minDate, final Date maxDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(selectedDate);
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                presenter.onBirthdateChanged(year, month, dayOfMonth, minDate, maxDate);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));
        DatePicker datePicker = datePickerDialog.getDatePicker();
        datePicker.setMinDate(minDate.getTime());
        datePicker.setMaxDate(maxDate.getTime());
        datePickerDialog.show();
    }

    @Override
    public void showBirthdatePickerDialog(Date selectedDate, final Date maxDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(selectedDate);
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                presenter.onBirthdateChanged(year, month, dayOfMonth, maxDate);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));
        DatePicker datePicker = datePickerDialog.getDatePicker();
        datePicker.setMaxDate(maxDate.getTime());
        datePickerDialog.show();
    }

    @Override
    public void showPassengerChildBirthdateShouldMoreThan2Years(int resId) {
        showMessageErrorInSnackbar(resId);
    }

    @Override
    public void showPassengerInfantBirthdateShouldNoMoreThan2Years(int resId) {
        showMessageErrorInSnackbar(resId);
    }

    @Override
    public void showPassengerAdultBirthdateShouldMoreThan12Years(int resId) {
        showMessageErrorInSnackbar(resId);
    }

    @Override
    public void showPassengerNameEmptyError(int resId) {
        showMessageErrorInSnackbar(resId);
    }

    @Override
    public void showPassengerFirstNameShouldAlphabetAndSpaceOnlyError(int resId) {
        showMessageErrorInSnackbar(resId);
    }

    @Override
    public void showPassengerFirstNameShouldNoMoreThanMaxError(int resId) {
        showMessageErrorInSnackbar(resId);
    }

    @Override
    public void showPassengerLastNameShouldSameWithFirstNameError(int resId) {
        showMessageErrorInSnackbar(resId);
    }

    @Override
    public void showPassengerLastNameEmptyError(int resId) {
        showMessageErrorInSnackbar(resId);
    }

    @Override
    public void showPassengerLastNameShouldOneWordError(int resId) {
        showMessageErrorInSnackbar(resId);
    }

    @Override
    public void showPassengerLastNameShouldAlphabetAndSpaceOnlyError(int resId) {
        showMessageErrorInSnackbar(resId);
    }

    @Override
    public void showPassengerTitleEmptyError(int resId) {
        showMessageErrorInSnackbar(resId);
    }

    @Override
    public void showPassengerBirthdateEmptyError(int resId) {
        showMessageErrorInSnackbar(resId);
    }

    @Override
    public void onSuccessUpdatePassengerData() {
        getActivity().setResult(Activity.RESULT_OK);
        getActivity().finish();
    }

    @Override
    public String getRequestId() {
        return getArguments().getString(EXTRA_REQUEST_ID);
    }

    private void showMessageErrorInSnackbar(int resId) {
        NetworkErrorHelper.showRedCloseSnackbar(getActivity(), getString(resId));
    }
}
