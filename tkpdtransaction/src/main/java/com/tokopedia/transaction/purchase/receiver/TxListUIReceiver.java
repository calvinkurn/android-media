package com.tokopedia.transaction.purchase.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * @author anggaprasetiyo on 9/20/16.
 */
public class TxListUIReceiver extends BroadcastReceiver {
    public static final String FILTER_ACTION = TxListUIReceiver.class.getCanonicalName()
            + ".FILTER_ACTION";
    public static final String EXTRA_ACTION_DO = "EXTRA_ACTION_DO";
    public static final int ACTION_DO_FORCE_REFRESH_LIST = 1;

    private final ActionListener actionListener;

    public TxListUIReceiver(ActionListener actionListener) {
        this.actionListener = actionListener;
    }

    public interface ActionListener {
        void forceRefreshListData();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equalsIgnoreCase(FILTER_ACTION)) {
            switch (intent.getIntExtra(EXTRA_ACTION_DO, ACTION_DO_FORCE_REFRESH_LIST)) {
                case ACTION_DO_FORCE_REFRESH_LIST:
                    actionListener.forceRefreshListData();
                    break;
            }
        }
    }

    public static void sendBroadcastForceRefreshListData(Context context) {
        Intent intent = new Intent(FILTER_ACTION);
        intent.putExtra(EXTRA_ACTION_DO, ACTION_DO_FORCE_REFRESH_LIST);
        context.sendBroadcast(intent);
    }
}
