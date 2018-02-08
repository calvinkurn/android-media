package com.tokopedia.flight.search.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

import com.tokopedia.flight.R;
import com.tokopedia.flight.airport.data.source.db.model.FlightAirportDB;
import com.tokopedia.flight.booking.view.activity.FlightBookingActivity;
import com.tokopedia.flight.common.constant.FlightFlowConstant;
import com.tokopedia.flight.common.constant.FlightFlowExtraConstant;
import com.tokopedia.flight.common.util.FlightAnalytics;
import com.tokopedia.flight.common.util.FlightDateUtil;
import com.tokopedia.flight.common.util.FlightFlowUtil;
import com.tokopedia.flight.common.view.BaseFlightActivity;
import com.tokopedia.flight.dashboard.view.fragment.viewmodel.FlightPassengerViewModel;
import com.tokopedia.flight.search.view.fragment.FlightSearchFragment;
import com.tokopedia.flight.search.view.model.FlightSearchPassDataViewModel;


public class FlightSearchActivity extends BaseFlightActivity
        implements FlightSearchFragment.OnFlightSearchFragmentListener {
    protected static final String EXTRA_PASS_DATA = "EXTRA_PASS_DATA";
    private static final int REQUEST_CODE_BOOKING = 10;
    private static final int REQUEST_CODE_RETURN = 11;
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
    public String getScreenName() {
        return FlightAnalytics.Screen.SEARCH;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initializeDataFromIntent();
        super.onCreate(savedInstanceState);

        setupFlightToolbar();
    }

    private void initializeDataFromIntent() {
        passDataViewModel = getIntent().getParcelableExtra(EXTRA_PASS_DATA);
        initializeToolbarData();
    }

    protected void initializeToolbarData() {
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
        toolbar.setSubtitleTextColor(ContextCompat.getColor(this, R.color.grey_500));
        String title = getDepartureAirport().getCityName() + " ➝ " + getArrivalAirport().getCityName();
        String subtitle = dateString + " | " + passengerString + " | " + classString;
        updateTitle(title, subtitle);
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
            startActivityForResult(FlightBookingActivity.getCallingIntent(this, passDataViewModel, selectedFlightID), REQUEST_CODE_BOOKING);
        } else {
            startActivityForResult(FlightSearchReturnActivity.getCallingIntent(this, passDataViewModel, selectedFlightID), REQUEST_CODE_RETURN);
        }
    }

    @Override
    public void changeDate(FlightSearchPassDataViewModel flightSearchPassDataViewModel) {
        this.passDataViewModel = flightSearchPassDataViewModel;
        initializeToolbarData();
        setupFlightToolbar();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_RETURN:
            case REQUEST_CODE_BOOKING:
                if (data != null) {
                    switch (data.getIntExtra(FlightFlowExtraConstant.EXTRA_FLOW_DATA, 0)) {
                        case FlightFlowConstant.PRICE_CHANGE:
                            Fragment fragment = getFragment();
                            if (fragment instanceof FlightSearchFragment) {
                                ((FlightSearchFragment) fragment).loadInitialData();
                            }
                            break;
                        case FlightFlowConstant.EXPIRED_JOURNEY:
                            FlightFlowUtil.actionSetResultAndClose(this,
                                    getIntent(),
                                    FlightFlowConstant.EXPIRED_JOURNEY
                            );
                            break;
                    }
                }
                break;
        }
    }
}
