package com.tokopedia.topads;

import android.content.Context;
import android.content.Intent;

import com.tokopedia.topads.dashboard.view.activity.TopAdsDashboardActivity;

/**
 * Created by nakama on 12/12/17.
 */

public class TkpdTopAds {
    public static Intent getIntentTopAdsDashboard(Context context){
        return new Intent(context, TopAdsDashboardActivity.class);
    }
}
