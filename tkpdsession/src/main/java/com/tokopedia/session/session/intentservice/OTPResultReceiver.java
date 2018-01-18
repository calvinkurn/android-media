package com.tokopedia.session.session.intentservice;

import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;

/**
 * Created by nisie on 12/20/16.
 */
@Deprecated
public class OTPResultReceiver extends ResultReceiver {

    private OTPResultReceiver.Receiver mReceiver;

    public OTPResultReceiver(Handler handler) {
        super(handler);
    }

    public void setReceiver(OTPResultReceiver.Receiver receiver) {
        mReceiver = receiver;
    }

    public interface Receiver {
        public void onReceiveResult(int resultCode, Bundle resultData);
    }

    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {
        if (mReceiver != null) {
            mReceiver.onReceiveResult(resultCode, resultData);
        }
    }
}
