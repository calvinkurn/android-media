package com.tokopedia.flight.orderlist.view;

import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;

/**
 * Created by zulfikarrahman on 11/28/17.
 */

public class FlightOrderListActivity extends BaseSimpleActivity {
    @Override
    protected Fragment getNewFragment() {
        return FlightOrderListFragment.createInstance();
    }
}
