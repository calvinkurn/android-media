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
import com.tokopedia.flight.booking.view.adapter.FlightBookingPassengerAdapter;
import com.tokopedia.flight.booking.view.presenter.FlightBookingContract;
import com.tokopedia.flight.booking.view.presenter.FlightBookingPresenter;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingPassengerViewModel;
import com.tokopedia.flight.booking.widget.CardWithActionView;

import javax.inject.Inject;

/**
 * A simple {@link Fragment} subclass.
 */
public class FlightBookingFragment extends BaseDaggerFragment implements FlightBookingContract.View, FlightBookingPassengerAdapter.OnClickListener {

    private static final int REQUEST_CODE_PASSENGER = 1;
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

    @Inject
    FlightBookingPresenter presenter;

    public static FlightBookingFragment newInstance() {
        return new FlightBookingFragment();
    }


    public FlightBookingFragment() {
        // Required empty public constructor
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

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onButtonSubmitClicked();
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.attachView(this);
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
}
