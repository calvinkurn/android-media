package com.tokopedia.transaction.cart.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.tokopedia.transaction.cart.model.toppaydata.TopPayParameterData;

/**
 * @author anggaprasetiyo on 11/23/16.
 */
public class CartBroadcastReceiver extends BroadcastReceiver {
    public static final String ACTION_TOP_PAY
            = CartBroadcastReceiver.class.getCanonicalName() + ".ACTION_GET_PARAMETER_TOP_PAY";

    public static final int RESULT_CODE_TOP_PAY_SUCCESS = 1;
    public static final int RESULT_CODE_TOP_PAY_ERROR = 0;
    public static final int RESULT_CODE_TOP_PAY_NO_CONNECTION = 2;
    public static final int RESULT_CODE_TOP_PAY_PROCESS_ONGOING = 3;


    public static final String EXTRA_RESULT_CODE_TOP_PAY_ACTION
            = "EXTRA_RESULT_CODE_TOP_PAY_ACTION";
    public static final String EXTRA_TOP_PAY_PARAMETER_DATA_TOP_PAY_ACTION
            = "EXTRA_TOP_PAY_PARAMETER_DATA_TOP_PAY_ACTION";
    public static final String EXTRA_MESSAGE_TOP_PAY_ACTION = "EXTRA_MESSAGE_TOP_PAY_ACTION";

    public CartBroadcastReceiver(Object listener) {
        if (listener instanceof ActionTopPayListener) {
            this.topPayListener = (ActionTopPayListener) listener;
        }
    }

    private ActionTopPayListener topPayListener;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(ACTION_TOP_PAY)) {
            int resultCode = intent.getExtras().getInt(EXTRA_RESULT_CODE_TOP_PAY_ACTION, 1);
            switch (resultCode) {
                case RESULT_CODE_TOP_PAY_SUCCESS:
                    topPayListener.onGetParameterTopPaySuccess(
                            (TopPayParameterData) intent.getExtras().getParcelable(
                                    EXTRA_TOP_PAY_PARAMETER_DATA_TOP_PAY_ACTION)
                    );
                    break;
                case RESULT_CODE_TOP_PAY_ERROR:
                    topPayListener.onGetParameterTopPayFailed(
                            intent.getExtras().getString(EXTRA_MESSAGE_TOP_PAY_ACTION)
                    );
                    break;
                case RESULT_CODE_TOP_PAY_NO_CONNECTION:
                    topPayListener.onGetParameterTopPayNoConnection(
                            intent.getExtras().getString(EXTRA_MESSAGE_TOP_PAY_ACTION)
                    );
                    break;
                case RESULT_CODE_TOP_PAY_PROCESS_ONGOING:
                    topPayListener.onGetParameterTopPayOngoing(
                            intent.getExtras().getString(EXTRA_MESSAGE_TOP_PAY_ACTION)
                    );
                    break;
            }
        }
    }

    public interface ActionTopPayListener {
        void onGetParameterTopPaySuccess(TopPayParameterData data);

        void onGetParameterTopPayFailed(String message);

        void onGetParameterTopPayNoConnection(String message);

        void onGetParameterTopPayOngoing(String message);
    }
}
