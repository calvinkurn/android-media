package com.tokopedia.flight.search.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.flight.R;
import com.tokopedia.flight.common.util.FlightDateUtil;
import com.tokopedia.flight.dashboard.view.fragment.viewmodel.FlightPassengerViewModel;
import com.tokopedia.flight.search.view.fragment.FlightSearchFragment;
import com.tokopedia.flight.search.view.model.FlightSearchPassDataViewModel;

/**
 * Created by User on 10/26/2017.
 */

public class FlightSearchActivity extends BaseSimpleActivity {
    private static final String EXTRA_PASS_DATA = "EXTRA_PASS_DATA";
    private String departureLocation;
    private String arrivalLocation;
    private String dateString;
    private String passengerString;
    private String classString;

    private FlightSearchPassDataViewModel passDataViewModel;

    public static void start(Context context) {
        Intent intent = new Intent(context, FlightSearchActivity.class);
        context.startActivity(intent);
    }

    public static Intent getCallingIntent(Activity activity, FlightSearchPassDataViewModel passDataViewModel) {
        Intent intent = new Intent(activity, FlightSearchActivity.class);
        intent.putExtra(EXTRA_PASS_DATA, passDataViewModel);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        passDataViewModel = getIntent().getParcelableExtra(EXTRA_PASS_DATA);
        String departureCode = passDataViewModel.getDepartureAirport().getAirportId();
        if (TextUtils.isEmpty(departureCode)) {
            departureCode = passDataViewModel.getDepartureAirport().getCityCode();
        }
        departureLocation = passDataViewModel.getDepartureAirport().getCityName() + " (" + departureCode + ")";
        String arrivalCode = passDataViewModel.getArrivalAirport().getAirportId();
        if (TextUtils.isEmpty(arrivalCode)) {
            arrivalCode = passDataViewModel.getArrivalAirport().getCityCode();
        }
        arrivalLocation = passDataViewModel.getArrivalAirport().getCityName() + " (" + arrivalCode + ")";
        dateString = FlightDateUtil.formatDate(
                FlightDateUtil.DEFAULT_FORMAT,
                FlightDateUtil.DEFAULT_VIEW_FORMAT,
                passDataViewModel.getDepartureDate()
        );
        passengerString = buildPassengerTextFormatted(passDataViewModel.getFlightPassengerViewModel());
        classString = passDataViewModel.getFlightClass().getTitle();

        setupFlightToolbar();
    }

    private void setupFlightToolbar() {
        toolbar.setContentInsetStartWithNavigation(0);
        toolbar.setSubtitleTextColor(ContextCompat.getColor(this, R.color.tkpd_dark_gray));
        String title = departureLocation + " ➝ " + arrivalLocation;
        String subtitle = dateString + " | " + passengerString + " | " + classString;
        updateTitle(title, subtitle);
    }

    @Override
    protected boolean isToolbarWhite() {
        return true;
    }

    @Override
    protected Fragment getNewFragment() {
        return FlightSearchFragment.newInstance(passDataViewModel);
    }

    private String buildPassengerTextFormatted(FlightPassengerViewModel passData) {
        String passengerFmt = "";
        if (passData.getAdult() > 0) {
            passengerFmt = passData.getAdult() + " " + getString(R.string.flight_dashboard_adult_passenger);
            if (passData.getChildren() > 0) {
                passengerFmt += ", " + passData.getChildren() + " " + getString(R.string.flight_dashboard_adult_children);
            }
            if (passData.getInfant() > 0) {
                passengerFmt += ", " + passData.getInfant() + " " + getString(R.string.flight_dashboard_adult_infant);
            }
        }
        return passengerFmt;
    }
}
