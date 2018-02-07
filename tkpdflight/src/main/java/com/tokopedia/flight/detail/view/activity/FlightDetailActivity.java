package com.tokopedia.flight.detail.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.activity.BaseTabActivity;
import com.tokopedia.flight.FlightComponentInstance;
import com.tokopedia.flight.R;
import com.tokopedia.flight.common.di.component.FlightComponent;
import com.tokopedia.flight.common.util.FlightAnalytics;
import com.tokopedia.flight.detail.view.fragment.FlightDetailFacilityFragment;
import com.tokopedia.flight.detail.view.fragment.FlightDetailFragment;
import com.tokopedia.flight.detail.view.fragment.FlightDetailPriceFragment;
import com.tokopedia.flight.detail.view.model.FlightDetailViewModel;

import javax.inject.Inject;

/**
 * Created by zulfikarrahman on 10/30/17.
 */

public class FlightDetailActivity extends BaseTabActivity {

    public static final String EXTRA_FLIGHT_SEARCH_MODEL = "EXTRA_FLIGHT_DETAIL_MODEL";
    public static final String EXTRA_FLIGHT_SELECTED = "EXTRA_FLIGHT_SELECTED";
    private static final String EXTRA_FLIGHT_DISPLAY_SUBMIT = "EXTRA_FLIGHT_DISPLAY_SUBMIT";
    @Inject
    FlightAnalytics flightAnalytics;
    private FlightDetailViewModel flightDetailViewModel;
    private Button buttonSubmit;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private AppBarLayout appBarLayout;
    private TextView departureAirportCode;
    private TextView departureAirportName;
    private TextView arrivalAirportCode;
    private TextView arrivalAirportName;
    private boolean isSubmitDisplayed;
    private FlightComponent component;

    public static Intent createIntent(Context context, FlightDetailViewModel flightDetailViewModel, boolean isSubmitDisplayed) {
        Intent intent = new Intent(context, FlightDetailActivity.class);
        intent.putExtra(EXTRA_FLIGHT_SEARCH_MODEL, flightDetailViewModel);
        intent.putExtra(EXTRA_FLIGHT_DISPLAY_SUBMIT, isSubmitDisplayed);
        return intent;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_flight_detail;
    }

    @Override
    protected void setupLayout(Bundle savedInstanceState) {
        flightDetailViewModel = getIntent().getParcelableExtra(EXTRA_FLIGHT_SEARCH_MODEL);
        isSubmitDisplayed = getIntent().getBooleanExtra(EXTRA_FLIGHT_DISPLAY_SUBMIT, true);
        super.setupLayout(savedInstanceState);
        buttonSubmit = (Button) findViewById(R.id.button_submit);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle(" ");
        appBarLayout = (AppBarLayout) findViewById(R.id.app_bar_layout);
        departureAirportCode = (TextView) findViewById(R.id.departure_airport_code);
        departureAirportName = (TextView) findViewById(R.id.departure_airport_name);
        arrivalAirportCode = (TextView) findViewById(R.id.arrival_airport_code);
        arrivalAirportName = (TextView) findViewById(R.id.arrival_airport_name);

        departureAirportCode.setText(flightDetailViewModel.getDepartureAirport());
        departureAirportName.setText(flightDetailViewModel.getDepartureAirportCity());
        arrivalAirportCode.setText(flightDetailViewModel.getArrivalAirport());
        arrivalAirportName.setText(flightDetailViewModel.getArrivalAirportCity());
        appBarLayout.addOnOffsetChangedListener(onAppbarOffsetChange());
        if (isSubmitDisplayed) {
            buttonSubmit.setVisibility(View.VISIBLE);
        } else {
            buttonSubmit.setVisibility(View.GONE);
        }
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResultAndFinish();
            }
        });
        tabLayout.addOnTabSelectedListener(getTabsListener());
        tabLayout.setupWithViewPager(viewPager);
    }

    @NonNull
    private TabLayout.OnTabSelectedListener getTabsListener() {
        return new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        flightAnalytics.eventDetailTabClick(flightDetailViewModel);
                        break;
                    case 1:
                        flightAnalytics.eventDetailFacilitiesTabClick(flightDetailViewModel);
                        break;
                    case 2:
                        flightAnalytics.eventDetailPriceTabClick(flightDetailViewModel);
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // no op
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // no op
            }
        };
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initInjector();
        super.onCreate(savedInstanceState);
    }

    protected FlightComponent getFlightComponent() {
        if (component == null) {
            component = FlightComponentInstance.getFlightComponent(getApplication());
        }
        return component;
    }

    private void initInjector() {
        getFlightComponent().inject(this);
    }


    private void setResultAndFinish() {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_FLIGHT_SELECTED, flightDetailViewModel.getId());
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    protected PagerAdapter getViewPagerAdapter() {
        return new FragmentPagerAdapter(getSupportFragmentManager()) {

            @Override
            public CharSequence getPageTitle(int position) {
                switch (position) {
                    case 0:
                        return getString(R.string.flight_label);
                    case 1:
                        return getString(R.string.flight_label_facility);
                    case 2:
                        return getString(R.string.flight_label_price);
                    default:
                        return super.getPageTitle(position);
                }
            }

            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0:
                        return FlightDetailFragment.createInstance(flightDetailViewModel);
                    case 1:
                        return FlightDetailFacilityFragment.createInstance(flightDetailViewModel);
                    case 2:
                        return FlightDetailPriceFragment.createInstance(flightDetailViewModel);
                    default:
                        return null;
                }
            }

            @Override
            public int getCount() {
                return 3;
            }
        };
    }

    private AppBarLayout.OnOffsetChangedListener onAppbarOffsetChange() {
        return new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    setUpToolbarTitle();
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbarLayout.setTitle(" ");
                    isShow = false;
                }
            }
        };
    }

    private void setUpToolbarTitle() {
        String title = flightDetailViewModel.getDepartureAirportCity() + " ➝ " + flightDetailViewModel.getArrivalAirportCity();
        collapsingToolbarLayout.setTitle(title);
    }

    @Override
    protected boolean isShowCloseButton() {
        return true;
    }

    @Override
    protected int getPageLimit() {
        return 3;
    }
}
