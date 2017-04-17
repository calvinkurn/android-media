package com.tokopedia.ride.ontrip.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by alvarisi on 4/17/17.
 */

public class ShareEtaReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String shareUrl = intent.getStringExtra("share");
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        share.putExtra(Intent.EXTRA_SUBJECT, "Klik link ini untuk menemukan perjalanan anda dari Tokopedia.");
        share.putExtra(Intent.EXTRA_TEXT, shareUrl);
        context.startActivity(Intent.createChooser(share, "Bagikan Link!"));
    }
}
