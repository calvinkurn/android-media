package com.tokopedia.flight.search.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.flight.search.view.fragment.FlightSearchFilterFragment;

/**
 * Created by nathan on 10/27/17.
 */

public class FlightSearchFilterActivity extends BaseSimpleActivity{

    public static Intent createInstance(Activity activity){
        Intent intent = new Intent(activity, FlightSearchFilterActivity.class);
        return intent;
    }

    @Override
    protected Fragment getNewFragment() {
        return FlightSearchFilterFragment.getInstance();
    }

    @Override
    protected boolean isToolbarWhite() {
        return true;
    }
}
