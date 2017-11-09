package com.tokopedia.flight.booking.view.fragment;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.utils.snackbar.SnackbarManager;
import com.tokopedia.flight.R;
import com.tokopedia.flight.booking.di.FlightBookingComponent;
import com.tokopedia.flight.booking.view.activity.FlightBookingPassengerActivity;
import com.tokopedia.flight.booking.view.activity.FlightBookingPhoneCodeActivity;
import com.tokopedia.flight.booking.view.adapter.FlightBookingPassengerAdapter;
import com.tokopedia.flight.booking.view.presenter.FlightBookingContract;
import com.tokopedia.flight.booking.view.presenter.FlightBookingPresenter;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingParamViewModel;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingPassengerViewModel;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingPhoneCodeViewModel;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingTripViewModel;
import com.tokopedia.flight.booking.widget.CardWithActionView;
import com.tokopedia.flight.search.view.model.FlightSearchPassDataViewModel;
import com.tokopedia.flight.search.view.model.FlightSearchViewModel;

import java.util.List;

import javax.inject.Inject;

/**
 * A simple {@link Fragment} subclass.
 */
public class FlightBookingFragment extends BaseDaggerFragment implements FlightBookingContract.View, FlightBookingPassengerAdapter.OnClickListener {
    private static final String EXTRA_TRIP_PASS_DATA = "EXTRA_TRIP_PASS_DATA";

    private static final int REQUEST_CODE_PASSENGER = 1;
    private static final int REQUEST_CODEP_PHONE_CODE = 2;

    private AppCompatTextView timeFinishOrderIndicatorTextView;
    private CardWithActionView departureInfoView;
    private CardWithActionView returnInfoView;
    private RecyclerView passengerRecyclerView;
    private AppCompatButton submitButton;
    private TextInputLayout tilContactName;
    private AppCompatEditText etContactName;
    private TextInputLayout tilContactEmail;
    private AppCompatEditText etContactEmail;
    private RelativeLayout contactPhoneNumberLayout;
    private AppCompatTextView contactPhoneNumberLabel;
    private AppCompatEditText etPhoneCountryCode;
    private AppCompatEditText etPhoneNumber;

    private FlightBookingTripViewModel flightBookingTripViewModel;
    private FlightBookingParamViewModel paramViewModel;

    @Inject
    FlightBookingPresenter presenter;

    private FlightBookingPassengerAdapter adapter;


    public static FlightBookingFragment newInstance(FlightBookingTripViewModel flightBookingTripViewModel) {
        FlightBookingFragment fragment = new FlightBookingFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_TRIP_PASS_DATA, flightBookingTripViewModel);
        fragment.setArguments(bundle);
        return fragment;
    }


    public FlightBookingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        flightBookingTripViewModel = getArguments().getParcelable(EXTRA_TRIP_PASS_DATA);
        paramViewModel = new FlightBookingParamViewModel();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_flight_booking, container, false);
        timeFinishOrderIndicatorTextView = (AppCompatTextView) view.findViewById(R.id.tv_time_finish_order_indicator);
        departureInfoView = (CardWithActionView) view.findViewById(R.id.cwa_departure_info);
        returnInfoView = (CardWithActionView) view.findViewById(R.id.cwa_return_info);
        passengerRecyclerView = (RecyclerView) view.findViewById(R.id.rv_passengers);
        submitButton = (AppCompatButton) view.findViewById(R.id.button_submit);
        tilContactName = (TextInputLayout) view.findViewById(R.id.til_contact_name);
        etContactName = (AppCompatEditText) view.findViewById(R.id.et_contact_name);
        tilContactEmail = (TextInputLayout) view.findViewById(R.id.til_contact_email);
        etContactEmail = (AppCompatEditText) view.findViewById(R.id.et_contact_email);
        contactPhoneNumberLayout = (RelativeLayout) view.findViewById(R.id.contact_phone_number_layout);
        contactPhoneNumberLabel = (AppCompatTextView) view.findViewById(R.id.contact_phone_number_label);
        etPhoneCountryCode = (AppCompatEditText) view.findViewById(R.id.et_phone_country_code);
        etPhoneNumber = (AppCompatEditText) view.findViewById(R.id.et_phone_number);

        etPhoneCountryCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(FlightBookingPhoneCodeActivity.getCallingIntent(getActivity()), REQUEST_CODEP_PHONE_CODE);
            }
        });
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onButtonSubmitClicked();
            }
        });

        adapter = new FlightBookingPassengerAdapter();
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        passengerRecyclerView.setLayoutManager(layoutManager);
        passengerRecyclerView.setHasFixedSize(true);
        passengerRecyclerView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.attachView(this);
        presenter.initialize();
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
    public void onChangePassengerData(FlightBookingPassengerViewModel viewModel) {
        startActivityForResult(FlightBookingPassengerActivity.getCallingIntent(getActivity(), viewModel), REQUEST_CODE_PASSENGER);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_PASSENGER:
                    FlightBookingPassengerViewModel passengerViewModel = data.getParcelableExtra(FlightBookingPassengerActivity.EXTRA_PASSENGER);
                    presenter.onPassengerResultReceived(passengerViewModel);
                    break;
                case REQUEST_CODEP_PHONE_CODE:
                    FlightBookingPhoneCodeViewModel phoneCodeViewModel = data.getParcelableExtra(FLightBookingPhoneCodeFragment.EXTRA_SELECTED_PHONE_CODE);
                    presenter.onPhoneCodeResultReceived(phoneCodeViewModel);
                    break;
            }
        }
    }

    @Override
    public String getContactName() {
        return etContactName.getText().toString().trim();
    }

    @Override
    public void showContactNameEmptyError(@StringRes int resId) {
        showMessageErrorInSnackBar(resId);
    }

    @Override
    public String getContactEmail() {
        return etContactEmail.getText().toString().trim();
    }

    @Override
    public void showContactEmailEmptyError(@StringRes int resId) {
        showMessageErrorInSnackBar(resId);
    }

    @Override
    public void showContactEmailInvalidError(@StringRes int resId) {
        showMessageErrorInSnackBar(resId);
    }

    @Override
    public String getContactPhoneNumber() {
        return etPhoneNumber.getText().toString().trim();
    }

    @Override
    public void showContactPhoneNumberEmptyError(@StringRes int resId) {
        showMessageErrorInSnackBar(resId);
    }

    @Override
    public FlightBookingTripViewModel getCurrentTripViewModel() {
        return flightBookingTripViewModel;
    }

    @Override
    public void showAndRenderReturnTripCardDetail(FlightSearchPassDataViewModel searchParam, FlightSearchViewModel returnTrip) {
        returnInfoView.setVisibility(View.VISIBLE);
        returnInfoView.setContent(returnTrip.getDepartureAirport() + "-" + returnTrip.getArrivalAirport());
        returnInfoView.setContentInfo(searchParam.getDepartureDate());
        String airLineSection = "";
        boolean isTransit = false;
        if (returnTrip.getAirlineList().size() > 1) {
            isTransit = true;
            airLineSection = getString(R.string.flight_booking_multiple_airline_trip_card);
        } else {
            airLineSection = returnTrip.getAirlineList().get(0).getName();
        }
        returnInfoView.setSubContent(airLineSection);

        String tripInfo = "";
        if (isTransit) {
            tripInfo += String.format("| %d %s", returnTrip.getAirlineList().size(), getString(R.string.flight_booking_transit_trip_card));
        } else {
            tripInfo += String.format("| %d %s", returnTrip.getAirlineList().size(), getString(R.string.flight_booking_directly_trip_card));
        }
        tripInfo += String.format("| %s %s - %s %s", returnTrip.getDepartureTime(), returnTrip.getDepartureAirport(), returnTrip.getArrivalTime(), returnTrip.getArrivalAirport());
        returnInfoView.setSubContentInfo(tripInfo);
    }

    @Override
    public void showAndRenderDepartureTripCardDetail(FlightSearchPassDataViewModel searchParam, FlightSearchViewModel returnTrip) {
        departureInfoView.setVisibility(View.VISIBLE);
        departureInfoView.setContent(returnTrip.getDepartureAirport() + "-" + returnTrip.getArrivalAirport());
        departureInfoView.setContentInfo(searchParam.getDepartureDate());
        String airLineSection = "";
        boolean isTransit = false;
        if (returnTrip.getAirlineList().size() > 1) {
            isTransit = true;
            airLineSection = getString(R.string.flight_booking_multiple_airline_trip_card);
        } else {
            airLineSection = returnTrip.getAirlineList().get(0).getName();
        }
        departureInfoView.setSubContent(airLineSection);

        String tripInfo = "";
        if (isTransit) {
            tripInfo += String.format("| %d %s", returnTrip.getAirlineList().size(), getString(R.string.flight_booking_transit_trip_card));
        } else {
            tripInfo += String.format("| %d %s", returnTrip.getAirlineList().size(), getString(R.string.flight_booking_directly_trip_card));
        }
        tripInfo += String.format("| %s %s - %s %s", returnTrip.getDepartureTime(), returnTrip.getDepartureAirport(), returnTrip.getArrivalTime(), returnTrip.getArrivalAirport());
        departureInfoView.setSubContentInfo(tripInfo);
    }

    @Override
    public void renderPassengersList(List<FlightBookingPassengerViewModel> passengerViewModels) {
        adapter.addPassengers(passengerViewModels);
    }

    @Override
    public FlightBookingParamViewModel getCurrentBookingParam() {
        return paramViewModel;
    }

    @Override
    public void renderPhoneCodeView(String countryPhoneCode) {
        etPhoneCountryCode.setText(countryPhoneCode);
    }

    private void showMessageErrorInSnackBar(int resId) {
        Snackbar snackBar = SnackbarManager.make(getActivity(),
                getString(resId), Snackbar.LENGTH_LONG)
                .setAction("Tutup", null);
        Button snackBarAction = (Button) snackBar.getView().findViewById(android.support.design.R.id.snackbar_action);
        snackBarAction.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
        snackBar.getView().setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.red_500));
        snackBar.show();
    }
}
