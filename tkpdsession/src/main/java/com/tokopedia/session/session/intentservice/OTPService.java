package com.tokopedia.session.session.intentservice;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.otp.OTPRetrofitInteractor;
import com.tokopedia.core.otp.OTPRetrofitInteractorImpl;
import com.tokopedia.core.service.DownloadService;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.session.R;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by nisie on 12/20/16.
 */
@Deprecated
public class OTPService extends IntentService {


    static String INTENT_NAME = "OTPService";
    public static String EXTRA_BUNDLE = "EXTRA_BUNDLE";
    static String EXTRA_RECEIVER = "EXTRA_RECEIVER";
    public static String EXTRA_ERROR = "EXTRA_ERROR";

    public static final int ACTION_REQUEST_OTP = 101;
    public static final int ACTION_REQUEST_OTP_WITH_CALL = 102;
    int ACTION_VERIFY_OTP = 103;

    int STATUS_SUCCESS = 1;
    int STATUS_ERROR = 2;

    OTPRetrofitInteractor networkInteractor;
    private Map<String, String> paramRequestOTPWithPhone;

    public OTPService() {
        super(INTENT_NAME);
        this.networkInteractor = new OTPRetrofitInteractorImpl();
    }

    public static void startAction(Context context, Bundle bundle,
                                   OTPResultReceiver receiver, int type) {
        Intent intent = new Intent(context, OTPService.class);
        intent.putExtra(DownloadService.TYPE, type);
        intent.putExtra(EXTRA_BUNDLE, bundle);
        intent.putExtra(EXTRA_RECEIVER, receiver);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            int action = intent.getIntExtra(DownloadService.TYPE, 0);
            Bundle bundle = intent.getBundleExtra(EXTRA_BUNDLE);
            ResultReceiver receiver = intent.getParcelableExtra(EXTRA_RECEIVER);

            switch (action) {
                case ACTION_REQUEST_OTP:
                    handleRequestOTP(bundle, receiver, action);
                    break;
                case ACTION_REQUEST_OTP_WITH_CALL:
                    handleRequestOTPWithCall(bundle, receiver, action);
                    break;
                default:
                    throw new UnsupportedOperationException("Unknown Action");
            }

        } else {
            CommonUtils.dumper("Failed onHandle Intent");
        }
    }

    private void handleRequestOTPWithCall(Bundle bundle, final ResultReceiver receiver, int action) {
        final Bundle resultData = new Bundle();
        resultData.putInt(DownloadService.TYPE, action);

        networkInteractor.requestOTPWithCall(getBaseContext(),
                AuthUtil.generateParams(getApplicationContext(),
                        getParamRequestOTPWithCall(),
                        SessionHandler.getTempLoginSession(getApplicationContext())),
                new OTPRetrofitInteractor.RequestOTPWithCallListener() {
                    @Override
                    public void onSuccess(String message) {
                        resultData.putString(EXTRA_BUNDLE, message);
                        receiver.send(STATUS_SUCCESS, resultData);
                    }

                    @Override
                    public void onTimeout() {
                        resultData.putString(EXTRA_ERROR, getString(R.string.msg_connection_timeout));
                        receiver.send(STATUS_ERROR, resultData);

                    }

                    @Override
                    public void onFailAuth() {
                        resultData.putString(EXTRA_ERROR, getString(R.string.default_request_error_forbidden_auth));
                        receiver.send(STATUS_ERROR, resultData);
                    }

                    @Override
                    public void onThrowable(Throwable e) {
                        resultData.putString(EXTRA_ERROR, getString(R.string.msg_no_connection));
                        receiver.send(STATUS_ERROR, resultData);
                    }

                    @Override
                    public void onError(String error) {
                        resultData.putString(EXTRA_ERROR, error);
                        receiver.send(STATUS_ERROR, resultData);
                    }

                    @Override
                    public void onNullData() {
                        resultData.putString(EXTRA_ERROR, getString(R.string.default_request_error_null_data));
                        receiver.send(STATUS_ERROR, resultData);
                    }

                    @Override
                    public void onNoConnection() {
                        resultData.putString(EXTRA_ERROR, getString(R.string.msg_no_connection));
                        receiver.send(STATUS_ERROR, resultData);
                    }

                });
    }

    private void handleRequestOTP(Bundle bundle, final ResultReceiver receiver, int action) {
        String userCheckQuestion2 = bundle.getString(EXTRA_BUNDLE);
        final Bundle resultData = new Bundle();
        resultData.putInt(DownloadService.TYPE, action);
        resultData.putAll(bundle);

        if (userCheckQuestion2 != null) {
            networkInteractor.requestOTP(getBaseContext(),
                    AuthUtil.generateParams(getApplicationContext(),
                            getParamRequestOTP(userCheckQuestion2), SessionHandler.getTempLoginSession(getApplicationContext())),
                    new OTPRetrofitInteractor.RequestOTPListener() {
                        @Override
                        public void onSuccess() {
                            resultData.putString(EXTRA_BUNDLE, getString(R.string.success_send_otp));
                            receiver.send(STATUS_SUCCESS, resultData);
                        }

                        @Override
                        public void onTimeout() {
                            resultData.putString(EXTRA_ERROR, getString(R.string.msg_connection_timeout));
                            receiver.send(STATUS_ERROR, resultData);
                        }

                        @Override
                        public void onFailAuth() {
                            resultData.putString(EXTRA_ERROR, getString(R.string.default_request_error_forbidden_auth));
                            receiver.send(STATUS_ERROR, resultData);
                        }

                        @Override
                        public void onThrowable(Throwable e) {
                            resultData.putString(EXTRA_ERROR, getString(R.string.msg_no_connection));
                            receiver.send(STATUS_ERROR, resultData);
                        }

                        @Override
                        public void onError(String error) {
                            resultData.putString(EXTRA_ERROR, error);
                            receiver.send(STATUS_ERROR, resultData);
                        }

                        @Override
                        public void onNullData() {
                            resultData.putString(EXTRA_ERROR, getString(R.string.default_request_error_null_data));
                            receiver.send(STATUS_ERROR, resultData);
                        }

                        @Override
                        public void onNoConnection() {
                            resultData.putString(EXTRA_ERROR, getString(R.string.msg_no_connection));
                            receiver.send(STATUS_ERROR, resultData);
                        }

                    });
        }
    }

    public Map<String, String> getParamRequestOTP(String userCheckQuestion2) {
        HashMap<String, String> param = new HashMap<>();
        param.put("user_check_question_2", userCheckQuestion2);
        return param;
    }

    public Map<String, String> getParamRequestOTPWithCall() {
        HashMap<String, String> param = new HashMap<>();
        param.put("mode", "call");
        return param;
    }
}
