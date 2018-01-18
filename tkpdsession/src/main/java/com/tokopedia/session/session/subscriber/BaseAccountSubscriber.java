package com.tokopedia.session.session.subscriber;

import android.content.Context;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;

import com.tokopedia.core.R;
import com.tokopedia.core.network.retrofit.response.ResponseStatus;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.service.DownloadService;
import com.tokopedia.core.service.constant.DownloadServiceConstant;
import com.tokopedia.core.util.SessionHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.List;

import retrofit2.Response;

/**
 * Created by stevenfredian on 9/1/16.
 */
@Deprecated
public abstract class BaseAccountSubscriber extends rx.Subscriber<Response<TkpdResponse>> implements DownloadServiceConstant {

    public static final String TAG = "Subscriber A";

    protected Context context;
    protected int type;
    ResultReceiver receiver;
    SessionHandler sessionHandler;

    public BaseAccountSubscriber(Context context, int type, ResultReceiver receiver, SessionHandler sessionHandler) {
        this.context = context;
        this.type = type;
        this.receiver = receiver;
        this.sessionHandler = sessionHandler;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        String error;
        if (e instanceof UnknownHostException) {
            error = DownloadServiceConstant.noNetworkConnection;
        } else if (e instanceof SocketTimeoutException) {
            error = context.getString(R.string.default_request_error_timeout);
        } else if (e instanceof IOException) {
            error = context.getString(R.string.default_request_error_internal_server);
        } else {
            error = context.getString(R.string.default_request_error_unknown);
        }

        sendMessageErrorToReceiver(error);
    }

    @Override
    public void onNext(Response<TkpdResponse> responseData) {
        if (responseData.isSuccessful()) {
            TkpdResponse response = responseData.body();
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(response.getStringData());
            } catch (JSONException je) {
                Log.e(TAG, je.getLocalizedMessage());
            }
            if (!response.isError()) {
                onSuccessResponse(jsonObject);
            } else {
                sendMessageErrorToReceiver(response.getErrorMessages());
            }
        } else {
            onFailedResponse(responseData.code());
        }
    }

    public void sendMessageErrorToReceiver(List<String> messageError) {
        sendMessageErrorToReceiver(messageError.toString().replace("[", "").replace("]", ""));
    }

    public void sendMessageErrorToReceiver(String messageError) {
        Log.d(TAG," onMessageError " + messageError);
        Bundle resultData = new Bundle();
        resultData.putInt(TYPE, type);
        resultData.putString(MESSAGE_ERROR_FLAG, messageError);
        receiver.send(DownloadService.STATUS_ERROR, resultData);
    }

    private void onFailedResponse(int code) {
        String error;
        switch (code) {
            case ResponseStatus.SC_REQUEST_TIMEOUT:
            case ResponseStatus.SC_GATEWAY_TIMEOUT:
                error = context.getString(R.string.default_request_error_timeout);
                break;
            case ResponseStatus.SC_INTERNAL_SERVER_ERROR:
                error = context.getString(R.string.default_request_error_internal_server);
                break;
            default:
                error = context.getString(R.string.default_request_error_unknown);
                break;
        }
        sendMessageErrorToReceiver(error);
    }

    protected abstract void onSuccessResponse(JSONObject json);


}