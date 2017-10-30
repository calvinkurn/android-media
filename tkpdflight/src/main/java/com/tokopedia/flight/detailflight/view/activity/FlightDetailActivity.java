package com.tokopedia.flight.detailflight.view.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;

import com.tokopedia.abstraction.base.view.activity.BaseTabActivity;
import com.tokopedia.flight.R;
import com.tokopedia.flight.detailflight.view.fragment.FlightDetailFacilityFragment;
import com.tokopedia.flight.detailflight.view.fragment.FlightDetailFragment;
import com.tokopedia.flight.detailflight.view.fragment.FlightDetailPriceFragment;

/**
 * Created by zulfikarrahman on 10/30/17.
 */

public class FlightDetailActivity extends BaseTabActivity {

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
                        return FlightDetailFragment.createInstance();
                    case 1:
                        return FlightDetailFacilityFragment.createInstance();
                    case 2:
                        return FlightDetailPriceFragment.createInstance();
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
