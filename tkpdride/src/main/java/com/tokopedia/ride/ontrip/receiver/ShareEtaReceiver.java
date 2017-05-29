package com.tokopedia.ride.ontrip.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.TaskStackBuilder;

import com.tokopedia.core.gcm.Constants;
import com.tokopedia.ride.common.configuration.RideStatus;
import com.tokopedia.ride.common.ride.domain.model.RideRequest;
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
//
//        String shareUrl = intent.getStringExtra("share");
//        Intent share = new Intent(Intent.ACTION_SEND);
//        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//        share.setType("text/plain");
//        share.putExtra(Intent.EXTRA_SUBJECT, "Klik link ini untuk menemukan perjalanan anda dari Tokopedia.");
//        share.putExtra(Intent.EXTRA_TEXT, shareUrl);
//        context.startActivity(Intent.createChooser(share, "Bagikan Link!").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }
}
