package com.tokopedia.flight.search.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.flight.R;
import com.tokopedia.flight.search.view.fragment.FlightSearchFilterFragment;
import com.tokopedia.flight.search.view.model.FlightFilterModel;
import com.tokopedia.flight.search.view.model.statistic.FlightSearchStatisticModel;

/**
 * Created by nathan on 10/27/17.
 */

public class FlightSearchFilterActivity extends BaseSimpleActivity
        implements FlightSearchFilterFragment.OnFlightSearchFilterFragmentListener {


    public static final String EXTRA_IS_RETURNING = "is_return";
    public static final String EXTRA_STAT_MODEL = "stat_model";
    public static final String EXTRA_FILTER_MODEL = "filter_model";

    View buttonFilter;

    private boolean isReturning;
    private FlightSearchStatisticModel flightSearchStatisticModel;
    private FlightFilterModel flightFilterModel;

    public static void start (Context context,
                              boolean isReturning,
                              FlightSearchStatisticModel flightSearchStatisticModel,
                              FlightFilterModel flightFilterModel){
        Intent intent = createInstance(context, isReturning, flightSearchStatisticModel, flightFilterModel);
        context.startActivity(intent);
    }

    public static Intent createInstance(Context context,
                                        boolean isReturning,
                                        FlightSearchStatisticModel flightSearchStatisticModel,
                                        FlightFilterModel flightFilterModel){
        Intent intent = new Intent(context, FlightSearchFilterActivity.class);
        intent.putExtra(EXTRA_IS_RETURNING, isReturning);
        intent.putExtra(EXTRA_STAT_MODEL, flightSearchStatisticModel);
        intent.putExtra(EXTRA_FILTER_MODEL, flightFilterModel);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        isReturning = intent.getBooleanExtra(EXTRA_IS_RETURNING, false);
        flightSearchStatisticModel = intent.getParcelableExtra(EXTRA_STAT_MODEL);

        if (savedInstanceState == null) {
            flightFilterModel = intent.getParcelableExtra(EXTRA_FILTER_MODEL);
        } else {
            flightFilterModel = savedInstanceState.getParcelable(EXTRA_FILTER_MODEL);
        }

        buttonFilter = findViewById(R.id.button_filter);
        buttonFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonFilterClicked();
            }
        });
    }

    private void onButtonFilterClicked() {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected Fragment getNewFragment() {
        return FlightSearchFilterFragment.newInstance();
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_flight_filter;
    }

    @Override
    protected boolean isToolbarWhite() {
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(EXTRA_FILTER_MODEL, flightFilterModel);
    }

    @Override
    public void onTransitLabelClicked() {

    }

    @Override
    public void onAirlineLabelClicked() {

    }

    @Override
    public void onRefundLabelClicked() {

    }

    @Override
    public void onDepartureLabelClicked() {

    }

    @Override
    public FlightSearchStatisticModel getFlightSearchStatisticModel() {
        return flightSearchStatisticModel;
    }

    @Override
    public FlightFilterModel getFlightFilterModel() {
        return flightFilterModel;
    }

    @Override
    public void onFilterModelChanged(FlightFilterModel flightFilterModel) {
        this.flightFilterModel = flightFilterModel;
        //TODO rerun query
    }
}
