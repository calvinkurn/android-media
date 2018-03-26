package com.tokopedia.flight.passenger.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.design.component.Dialog;
import com.tokopedia.flight.FlightComponentInstance;
import com.tokopedia.flight.R;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingPassengerViewModel;
import com.tokopedia.flight.passenger.di.DaggerFlightPassengerComponent;
import com.tokopedia.flight.passenger.di.FlightPassengerComponent;
import com.tokopedia.flight.passenger.view.fragment.FlightPassengerUpdateFragment;

public class FlightPassengerUpdateActivity extends BaseSimpleActivity
        implements HasComponent<FlightPassengerComponent> {

    public static final String EXTRA_PASSENGER_VIEW_MODEL = "EXTRA_PASSENGER_VIEW_MODEL";
    public static final String EXTRA_DEPARTURE_DATE = "EXTRA_DEPARTURE_DATE";
    public static final String EXTRA_REQUEST_ID = "EXTRA_REQUEST_ID";

    public static Intent getCallingIntent(Activity activity,
                                          FlightBookingPassengerViewModel passengerViewModel,
                                          String departureDate, String requestId) {
        Intent intent = new Intent(activity, FlightPassengerUpdateActivity.class);
        intent.putExtra(EXTRA_PASSENGER_VIEW_MODEL, passengerViewModel);
        intent.putExtra(EXTRA_DEPARTURE_DATE, departureDate);
        intent.putExtra(EXTRA_REQUEST_ID, requestId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected Fragment getNewFragment() {
        FlightBookingPassengerViewModel flightBookingPassengerViewModel = getIntent().getExtras()
                .getParcelable(EXTRA_PASSENGER_VIEW_MODEL);
        return FlightPassengerUpdateFragment.newInstance(
                flightBookingPassengerViewModel,
                getIntent().getExtras().getString(EXTRA_DEPARTURE_DATE),
                getIntent().getExtras().getString(EXTRA_REQUEST_ID)
        );
    }

    @Override
    public FlightPassengerComponent getComponent() {
        return DaggerFlightPassengerComponent.builder()
                .flightComponent(FlightComponentInstance.getFlightComponent(getApplication()))
                .build();
    }

    @Override
    public void onBackPressed() {
        showCancelUpdateDialog();
    }

    private void showCancelUpdateDialog() {
        final Dialog dialog = new Dialog(this, Dialog.Type.PROMINANCE);
        dialog.setTitle(getString(R.string.flight_passenger_update_abort_dialog_title));
        dialog.setDesc(getString(R.string.flight_passenger_update_abort_dialog_description));
        dialog.setBtnOk("Lanjut ubah");
        dialog.setOnCancelClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                finish();
            }
        });
        dialog.setBtnCancel("Ya, batalkan");
        dialog.setOnOkClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
