package com.tokopedia.flight.search.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.flight.R;
import com.tokopedia.flight.airport.data.source.db.model.FlightAirportDB;
import com.tokopedia.flight.booking.view.activity.FlightBookingActivity;
import com.tokopedia.flight.common.util.FlightDateUtil;
import com.tokopedia.flight.dashboard.view.fragment.viewmodel.FlightPassengerViewModel;
import com.tokopedia.flight.search.view.fragment.FlightSearchFragment;
import com.tokopedia.flight.search.view.model.FlightSearchPassDataViewModel;


public class FlightSearchActivity extends BaseSimpleActivity
        implements FlightSearchFragment.OnFlightSearchFragmentListener{
    protected static final String EXTRA_PASS_DATA = "EXTRA_PASS_DATA";
    protected String dateString;
    protected String passengerString;
    protected String classString;

    protected FlightSearchPassDataViewModel passDataViewModel;

    public static void start(Context context, FlightSearchPassDataViewModel passDataViewModel) {
        Intent intent = getCallingIntent(context, passDataViewModel);
        context.startActivity(intent);
    }

    public static Intent getCallingIntent(Context context, FlightSearchPassDataViewModel passDataViewModel) {
        Intent intent = new Intent(context, FlightSearchActivity.class);
        intent.putExtra(EXTRA_PASS_DATA, passDataViewModel);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initializeDataFromIntent();
        super.onCreate(savedInstanceState);

        setupFlightToolbar();
    }

    protected void initializeDataFromIntent(){
        passDataViewModel = getIntent().getParcelableExtra(EXTRA_PASS_DATA);
        dateString = FlightDateUtil.formatDate(
                FlightDateUtil.DEFAULT_FORMAT,
                FlightDateUtil.DEFAULT_VIEW_FORMAT,
                passDataViewModel.getDepartureDate()
        );
        passengerString = buildPassengerTextFormatted(passDataViewModel.getFlightPassengerViewModel());
        classString = passDataViewModel.getFlightClass().getTitle();
    }

    protected FlightAirportDB getDepartureAirport() {
        return passDataViewModel.getDepartureAirport();
    }

    protected FlightAirportDB getArrivalAirport() {
        return passDataViewModel.getArrivalAirport();
    }

    private void setupFlightToolbar() {
        toolbar.setContentInsetStartWithNavigation(0);
        toolbar.setSubtitleTextColor(ContextCompat.getColor(this, R.color.tkpd_dark_gray));
        String title = getDepartureAirport().getCityName() + " ➝ " + getArrivalAirport().getCityName();
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

    protected String buildPassengerTextFormatted(FlightPassengerViewModel passData) {
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

    @Override
    public void selectFlight(String selectedFlightID) {
        if (passDataViewModel.isOneWay()) {
            startActivity(FlightBookingActivity.getCallingIntent(this, passDataViewModel, selectedFlightID));
        } else {
            FlightSearchReturnActivity.start(this, passDataViewModel, selectedFlightID);
        }
    }

}
