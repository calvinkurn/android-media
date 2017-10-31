package com.tokopedia.flight.detail.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;

import com.tokopedia.abstraction.base.view.activity.BaseTabActivity;
import com.tokopedia.flight.R;
import com.tokopedia.flight.detail.view.fragment.FlightDetailFacilityFragment;
import com.tokopedia.flight.detail.view.fragment.FlightDetailFragment;
import com.tokopedia.flight.detail.view.fragment.FlightDetailPriceFragment;
import com.tokopedia.flight.search.view.model.FlightSearchViewModel;

/**
 * Created by zulfikarrahman on 10/30/17.
 */

public class FlightDetailActivity extends BaseTabActivity {

    public static final String EXTRA_FLIGHT_SEARCH_MODEL = "EXTRA_FLIGHT_SEARCH_MODEL";

    private FlightSearchViewModel flightSearchViewModel;

    public static Intent createIntent(Context context, FlightSearchViewModel flightSearchViewModel){
        Intent intent = new Intent(context, FlightDetailActivity.class);
        intent.putExtra(EXTRA_FLIGHT_SEARCH_MODEL, flightSearchViewModel);
        return intent;
    }

    @Override
    protected void setupFragment(Bundle savedinstancestate) {
        flightSearchViewModel = getIntent().getParcelableExtra(EXTRA_FLIGHT_SEARCH_MODEL);
        super.setupFragment(savedinstancestate);
    }

    @Override
    protected PagerAdapter getViewPagerAdapter() {
        return new FragmentPagerAdapter(getSupportFragmentManager()) {

            @Override
            public CharSequence getPageTitle(int position) {
                switch (position){
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
                switch (position){
                    case 0:
                        return FlightDetailFragment.createInstance(flightSearchViewModel);
                    case 1:
                        return FlightDetailFacilityFragment.createInstance(flightSearchViewModel);
                    case 2:
                        return FlightDetailPriceFragment.createInstance(flightSearchViewModel);
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

    @Override
    protected boolean isToolbarWhite() {
        return true;
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
