package com.tokopedia.ride.ontrip.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.TaskStackBuilder;

import com.tokopedia.ride.ontrip.view.OnTripActivity;

/**
 * Created by alvarisi on 4/17/17.
 */

public class ShareEtaReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String url = intent.getStringExtra("share");
        TaskStackBuilder stackBuilder = OnTripActivity.getCallingIntentWithShareAction(context, url);
        stackBuilder.startActivities();
    }
}
