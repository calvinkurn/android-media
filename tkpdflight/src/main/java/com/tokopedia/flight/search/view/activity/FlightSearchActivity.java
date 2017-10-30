package com.tokopedia.flight.search.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.flight.R;
import com.tokopedia.flight.search.view.fragment.FlightSearchFragment;

/**
 * Created by User on 10/26/2017.
 */

public class FlightSearchActivity extends BaseSimpleActivity {

    private String departureLocation;
    private String arrivalLocation;
    private String dateString;
    private String passengerString;
    private String classString;

    public static void start(Context context){
        Intent intent = new Intent(context, FlightSearchActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        departureLocation = "Jakarta (CGK)";
        arrivalLocation = "London (LHR)";
        dateString = "19 Agustus 2017";
        passengerString = "1 Dewasa, 1 Anak";
        classString = "Ekonomi";

        setupFlightToolbar();
    }

    private void setupFlightToolbar() {
        toolbar.setContentInsetStartWithNavigation(0);
        toolbar.setSubtitleTextColor(ContextCompat.getColor(this, R.color.tkpd_dark_gray));
        String title = departureLocation + " ➝ " + arrivalLocation;
        String subtitle = dateString +" | " + passengerString + " | " + classString;
        updateTitle(title, subtitle);
    }

    @Override
    protected boolean isToolbarWhite() {
        return true;
    }

    @Override
    protected Fragment getNewFragment() {
        return FlightSearchFragment.newInstance();
    }
}
