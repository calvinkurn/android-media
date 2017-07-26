package com.tokopedia.ride.ontrip.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.tkpd.library.utils.CommonUtils;

/**
 * Created by alvarisi on 4/13/17.
 */

public class CallDriverReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        CommonUtils.dumper("CallDriverReceiver onReceive");
        Intent callIntent = new Intent(Intent.ACTION_DIAL);
        callIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        callIntent.setData(Uri.parse("tel:" + intent.getStringExtra("telp")));
        context.startActivity(callIntent);
    }
}
