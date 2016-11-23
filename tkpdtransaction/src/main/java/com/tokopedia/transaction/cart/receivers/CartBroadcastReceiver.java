package com.tokopedia.transaction.cart.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * @author anggaprasetiyo on 11/23/16.
 */

public class CartBroadcastReceiver extends BroadcastReceiver {
    public static final String ACTION = CartBroadcastReceiver.class.getCanonicalName() + ".ACTION";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(ACTION)) {

        }
    }

    public interface ActionListener {
        void onSuccessGetParameterTopPay();
    }
}
