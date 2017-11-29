package com.tokopedia.flight.search.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.tokopedia.flight.R;
import com.tokopedia.flight.airport.data.source.db.model.FlightAirportDB;
import com.tokopedia.flight.booking.view.activity.FlightBookingActivity;
import com.tokopedia.flight.common.util.FlightDateUtil;
import com.tokopedia.flight.search.view.fragment.FlightSearchFragment;
import com.tokopedia.flight.search.view.fragment.FlightSearchReturnFragment;
import com.tokopedia.flight.search.view.model.FlightSearchPassDataViewModel;

/**
 * Created by User on 10/26/2017.
 */

public class FlightSearchReturnActivity extends FlightSearchActivity implements FlightSearchFragment.OnFlightSearchFragmentListener {

    public static final String EXTRA_SEL_DEPARTURE_ID = "EXTRA_DEPARTURE_ID";

    private String selectedDepartureID;

    public static void start(Context context, FlightSearchPassDataViewModel passDataViewModel, String selectedDepartureID) {
        Intent intent = getCallingIntent(context, passDataViewModel, selectedDepartureID);
        context.startActivity(intent);
    }

    public static Intent getCallingIntent(Context context, FlightSearchPassDataViewModel passDataViewModel, String selectedDepartureID) {
        Intent intent = new Intent(context, FlightSearchReturnActivity.class);
        intent.putExtra(EXTRA_PASS_DATA, passDataViewModel);
        intent.putExtra(EXTRA_SEL_DEPARTURE_ID, selectedDepartureID);
        return intent;
    }

    @Override
    protected void initializeToolbarData() {
        selectedDepartureID = getIntent().getStringExtra(EXTRA_SEL_DEPARTURE_ID);

        dateString = FlightDateUtil.formatDate(
                FlightDateUtil.DEFAULT_FORMAT,
                FlightDateUtil.DEFAULT_VIEW_FORMAT,
                passDataViewModel.getReturnDate()
        );
        passengerString = buildPassengerTextFormatted(passDataViewModel.getFlightPassengerViewModel());
        classString = passDataViewModel.getFlightClass().getTitle();
    }

    protected FlightAirportDB getDepartureAirport() {
        return passDataViewModel.getArrivalAirport();
    }

    protected FlightAirportDB getArrivalAirport() {
        return passDataViewModel.getDepartureAirport();
    }

    @Override
    protected Fragment getNewFragment() {
        return FlightSearchReturnFragment.newInstance(passDataViewModel, selectedDepartureID);
    }

    @Override
    public void selectFlight(String selectedFlightID) {
        startActivity(FlightBookingActivity.getCallingIntent(this, passDataViewModel, selectedDepartureID, selectedFlightID));
    }

}
