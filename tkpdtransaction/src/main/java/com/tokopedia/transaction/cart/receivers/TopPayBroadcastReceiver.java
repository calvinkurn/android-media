package com.tokopedia.transaction.cart.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.tokopedia.transaction.common.data.cart.thankstoppaydata.ThanksTopPayData;
import com.tokopedia.transaction.cart.model.toppaydata.TopPayParameterData;

/**
 * @author anggaprasetiyo on 11/23/16.
 */
public class TopPayBroadcastReceiver extends BroadcastReceiver {
    @Deprecated
    public static final String ACTION_GET_PARAMETER_TOP_PAY
            = TopPayBroadcastReceiver.class.getCanonicalName() + ".ACTION_GET_PARAMETER_TOP_PAY";
    @Deprecated
    public static final String ACTION_GET_THANKS_TOP_PAY
            = TopPayBroadcastReceiver.class.getCanonicalName() + ".ACTION_GET_THANKS_TOP_PAY";
    public static final String ACTION_TOP_PAY = ".ACTION_TOP_PAY";

    public static final int RESULT_CODE_TOP_PAY_VERIFICATION_SUCCESS = 1;
    public static final int RESULT_CODE_TOP_PAY_VERIFICATION_ERROR = 0;
    public static final int RESULT_CODE_TOP_PAY_VERIFICATION_NO_CONNECTION = 2;
    public static final int RESULT_CODE_TOP_PAY_VERIFICATION_PROCESS_ONGOING = 3;
    public static final int RESULT_CODE_TOP_PAY_VERIFICATION_PAYMENT_NOT_VERIFIED = 4;

    public static final int RESULT_CODE_TOP_PAY_GET_PARAMETER_SUCCESS = 5;
    public static final int RESULT_CODE_TOP_PAY_GET_PARAMETER_ERROR = 6;
    public static final int RESULT_CODE_TOP_PAY_GET_PARAMETER_NO_CONNECTION = 7;
    public static final int RESULT_CODE_TOP_PAY_GET_PARAMETER_PROCESS_ONGOING = 8;

    public static final String EXTRA_RESULT_CODE_TOP_PAY_ACTION
            = "EXTRA_RESULT_CODE_TOP_PAY_ACTION";
    public static final String EXTRA_TOP_PAY_PARAMETER_DATA_TOP_PAY_ACTION
            = "EXTRA_TOP_PAY_PARAMETER_DATA_TOP_PAY_ACTION";
    public static final String EXTRA_TOP_PAY_THANKS_TOP_PAY_ACTION
            = "EXTRA_TOP_PAY_THANKS_TOP_PAY_ACTION";
    public static final String EXTRA_MESSAGE_TOP_PAY_ACTION = "EXTRA_MESSAGE_TOP_PAY_ACTION";
    public static final String EXTRA_PAYMENT_ID = "EXTRA_PAYMENT_ID";


    private ActionTopPayThanksListener topPayGetThanksListener;
    private ActionTopPayListener topPayListener;
    private ActionListener actionListener;

    public TopPayBroadcastReceiver(Object listener) {
        if (listener instanceof ActionListener) {
            this.actionListener = (ActionListener) listener;
        } else {
            if (listener instanceof ActionTopPayListener) {
                this.topPayListener = (ActionTopPayListener) listener;
            } else if (listener instanceof ActionTopPayThanksListener) {
                this.topPayGetThanksListener = (ActionTopPayThanksListener) listener;
            }
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(ACTION_GET_PARAMETER_TOP_PAY)) {
            int resultCode = intent.getExtras().getInt(EXTRA_RESULT_CODE_TOP_PAY_ACTION, 1);
            switch (resultCode) {
                case RESULT_CODE_TOP_PAY_GET_PARAMETER_SUCCESS:
                    topPayListener.onGetParameterTopPaySuccess(
                            (TopPayParameterData) intent.getExtras().getParcelable(
                                    EXTRA_TOP_PAY_PARAMETER_DATA_TOP_PAY_ACTION)
                    );
                    break;
                case RESULT_CODE_TOP_PAY_GET_PARAMETER_ERROR:
                    topPayListener.onGetParameterTopPayFailed(
                            intent.getExtras().getString(EXTRA_MESSAGE_TOP_PAY_ACTION)
                    );
                    break;
                case RESULT_CODE_TOP_PAY_GET_PARAMETER_NO_CONNECTION:
                    topPayListener.onGetParameterTopPayNoConnection(
                            intent.getExtras().getString(EXTRA_MESSAGE_TOP_PAY_ACTION)
                    );
                    break;
                case RESULT_CODE_TOP_PAY_GET_PARAMETER_PROCESS_ONGOING:
                    topPayListener.onGetParameterTopPayOngoing(
                            intent.getExtras().getString(EXTRA_MESSAGE_TOP_PAY_ACTION)
                    );
                    break;
            }
        } else if (intent.getAction().equals(ACTION_GET_THANKS_TOP_PAY)) {
            int resultCode = intent.getExtras().getInt(EXTRA_RESULT_CODE_TOP_PAY_ACTION, 1);
            switch (resultCode) {
                case RESULT_CODE_TOP_PAY_VERIFICATION_SUCCESS:
                    topPayGetThanksListener.onGetThanksTopPaySuccess(
                            (ThanksTopPayData) intent.getExtras().getParcelable(
                                    EXTRA_TOP_PAY_THANKS_TOP_PAY_ACTION
                            )
                    );
                    break;
                case RESULT_CODE_TOP_PAY_VERIFICATION_ERROR:
                    topPayGetThanksListener.onGetThanksTopPayFailed(
                            intent.getExtras().getString(EXTRA_MESSAGE_TOP_PAY_ACTION)
                    );
                    break;
                case RESULT_CODE_TOP_PAY_VERIFICATION_NO_CONNECTION:
                    topPayGetThanksListener.onGetThanksTopPayNoConnection(
                            intent.getExtras().getString(EXTRA_MESSAGE_TOP_PAY_ACTION)
                    );
                    break;
                case RESULT_CODE_TOP_PAY_VERIFICATION_PROCESS_ONGOING:
                    topPayGetThanksListener.onGetThanksTopPayOngoing(
                            intent.getExtras().getString(EXTRA_MESSAGE_TOP_PAY_ACTION)
                    );
                    break;
                case RESULT_CODE_TOP_PAY_VERIFICATION_PAYMENT_NOT_VERIFIED:
                    topPayGetThanksListener.onGetThanksTopPayNotValid(
                            intent.getExtras().getString(EXTRA_MESSAGE_TOP_PAY_ACTION)
                    );
                    break;
            }
        } else if (intent.getAction().equals(ACTION_TOP_PAY)) {
            int resultCode = intent.getExtras().getInt(EXTRA_RESULT_CODE_TOP_PAY_ACTION, 1);
            String paymentId = intent.getExtras().getString(EXTRA_PAYMENT_ID);
            switch (resultCode) {
                case RESULT_CODE_TOP_PAY_GET_PARAMETER_SUCCESS:
                    actionListener.onGetParameterTopPaySuccess(
                            (TopPayParameterData) intent.getExtras().getParcelable(
                                    EXTRA_TOP_PAY_PARAMETER_DATA_TOP_PAY_ACTION)
                    );
                    break;
                case RESULT_CODE_TOP_PAY_GET_PARAMETER_ERROR:
                    actionListener.onGetParameterTopPayFailed(
                            intent.getExtras().getString(EXTRA_MESSAGE_TOP_PAY_ACTION)
                    );
                    break;
                case RESULT_CODE_TOP_PAY_GET_PARAMETER_NO_CONNECTION:
                    actionListener.onGetParameterTopPayNoConnection(
                            intent.getExtras().getString(EXTRA_MESSAGE_TOP_PAY_ACTION)
                    );
                    break;
                case RESULT_CODE_TOP_PAY_GET_PARAMETER_PROCESS_ONGOING:
                    actionListener.onGetParameterTopPayOngoing(
                            intent.getExtras().getString(EXTRA_MESSAGE_TOP_PAY_ACTION)
                    );
                    break;
                case RESULT_CODE_TOP_PAY_VERIFICATION_SUCCESS:
                    actionListener.onGetThanksTopPaySuccess(
                            (ThanksTopPayData) intent.getExtras().getParcelable(
                                    EXTRA_TOP_PAY_THANKS_TOP_PAY_ACTION
                            )
                    );
                    break;
                case RESULT_CODE_TOP_PAY_VERIFICATION_ERROR:
                    actionListener.onGetThanksTopPayFailed(
                            intent.getExtras().getString(EXTRA_MESSAGE_TOP_PAY_ACTION),
                            paymentId
                    );
                    break;
                case RESULT_CODE_TOP_PAY_VERIFICATION_NO_CONNECTION:
                    actionListener.onGetThanksTopPayNoConnection(
                            intent.getExtras().getString(EXTRA_MESSAGE_TOP_PAY_ACTION),
                            paymentId
                    );
                    break;
                case RESULT_CODE_TOP_PAY_VERIFICATION_PROCESS_ONGOING:
                    actionListener.onGetThanksTopPayOngoing(
                            intent.getExtras().getString(EXTRA_MESSAGE_TOP_PAY_ACTION),
                            paymentId
                    );
                    break;
                case RESULT_CODE_TOP_PAY_VERIFICATION_PAYMENT_NOT_VERIFIED:
                    actionListener.onGetThanksTopPayNotValid(
                            intent.getExtras().getString(EXTRA_MESSAGE_TOP_PAY_ACTION),
                            paymentId
                    );
                    break;
            }
        }
    }

    /**
     * Sudah mampos
     *
     * @see ActionListener
     */
    @Deprecated
    public interface ActionTopPayListener {
        void onGetParameterTopPaySuccess(TopPayParameterData data);

        void onGetParameterTopPayFailed(String message);

        void onGetParameterTopPayNoConnection(String message);

        void onGetParameterTopPayOngoing(String message);
    }

    /**
     * Sudah mampos
     *
     * @see ActionListener
     */
    @Deprecated
    public interface ActionTopPayThanksListener {
        void onGetThanksTopPaySuccess(ThanksTopPayData data);

        void onGetThanksTopPayFailed(String message);

        void onGetThanksTopPayNotValid(String message);

        void onGetThanksTopPayNoConnection(String message);

        void onGetThanksTopPayOngoing(String message);
    }

    public interface ActionListener {
        void onGetParameterTopPaySuccess(TopPayParameterData data);

        void onGetParameterTopPayFailed(String message);

        void onGetParameterTopPayNoConnection(String message);

        void onGetParameterTopPayOngoing(String message);

        void onGetThanksTopPaySuccess(ThanksTopPayData data);

        void onGetThanksTopPayFailed(String message, String paymentId);

        void onGetThanksTopPayNotValid(String message, String paymentId);

        void onGetThanksTopPayNoConnection(String message, String paymentId);

        void onGetThanksTopPayOngoing(String message, String paymentId);
    }
}
