package com.tokopedia.flight.search.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.widget.Toast;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.flight.R;
import com.tokopedia.flight.common.util.FlightDateUtil;
import com.tokopedia.flight.search.view.fragment.FlightSearchFragment;
import com.tokopedia.flight.search.view.fragment.FlightSearchReturnFragment;
import com.tokopedia.flight.search.view.model.FlightSearchPassDataViewModel;

/**
 * Created by User on 10/26/2017.
 */

public class FlightSearchReturnActivity extends FlightSearchActivity implements FlightSearchFragment.OnFlightSearchFragmentListener{

    private static final String EXTRA_SEL_DEPARTURE_ID= "EXTRA_DEPARTURE_ID";

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initializeDataFromIntent() {
        passDataViewModel = getIntent().getParcelableExtra(EXTRA_PASS_DATA);
        selectedDepartureID = getIntent().getStringExtra(EXTRA_SEL_DEPARTURE_ID);

        String departureCode = passDataViewModel.getArrivalAirport().getAirportId();
        if (TextUtils.isEmpty(departureCode)) {
            departureCode = passDataViewModel.getArrivalAirport().getCityCode();
        }
        departureLocation = passDataViewModel.getArrivalAirport().getCityName() + " (" + departureCode + ")";

        String arrivalCode = passDataViewModel.getDepartureAirport().getAirportId();
        if (TextUtils.isEmpty(arrivalCode)) {
            arrivalCode = passDataViewModel.getDepartureAirport().getCityCode();
        }
        arrivalLocation = passDataViewModel.getDepartureAirport().getCityName() + " (" + arrivalCode + ")";
        dateString = FlightDateUtil.formatDate(
                FlightDateUtil.DEFAULT_FORMAT,
                FlightDateUtil.DEFAULT_VIEW_FORMAT,
                passDataViewModel.getReturnDate()
        );
        passengerString = buildPassengerTextFormatted(passDataViewModel.getFlightPassengerViewModel());
        classString = passDataViewModel.getFlightClass().getTitle();
    }

    @Override
    protected Fragment getNewFragment() {
        return FlightSearchReturnFragment.newInstance(passDataViewModel);
    }

    @Override
    public void selectFlight(String selectedFlightID) {
        Toast.makeText(this, "GO TO CART with id " + selectedDepartureID + " AND " + selectedFlightID, Toast.LENGTH_LONG).show();
    }
}
