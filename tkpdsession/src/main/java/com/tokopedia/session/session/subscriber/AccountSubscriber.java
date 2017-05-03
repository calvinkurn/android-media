package com.tokopedia.session.session.subscriber;

import android.os.Bundle;
import android.os.Parcelable;
import android.os.ResultReceiver;
import android.util.Log;

import com.google.gson.GsonBuilder;
import com.tokopedia.core.service.DownloadService;
import com.tokopedia.core.service.constant.DownloadServiceConstant;
import com.tokopedia.core.session.model.ErrorModel;
import com.tokopedia.core.session.model.InfoModel;
import com.tokopedia.core.session.model.TokenModel;
import com.tokopedia.core.util.SessionHandler;

import java.io.IOException;
import java.net.UnknownHostException;

import retrofit2.Response;

/**
 * Created by stevenfredian on 5/27/16.
 * <p>
 * migrate retrofit 2 by Angga.Prasetiyo
 */
public class AccountSubscriber extends rx.Subscriber<Response<String>> implements DownloadServiceConstant {

    public static final String TAG = "Steven";
    public static final String messageTAG = DownloadService.class.getSimpleName() + " ";

    int type;
    ResultReceiver receiver;
    SessionHandler sessionHandler;
    Parcelable parcelable;

    public AccountSubscriber(int type, ResultReceiver receiver, SessionHandler sessionHandler, Parcelable parcelable) {
        this.parcelable = parcelable;
        this.type = type;
        this.receiver = receiver;
        this.sessionHandler = sessionHandler;
    }

    public AccountSubscriber(int type, ResultReceiver receiver, SessionHandler sessionHandler) {
        this.type = type;
        this.receiver = receiver;
        this.sessionHandler = sessionHandler;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        Log.e(TAG, messageTAG + e.getLocalizedMessage());
        Bundle resultData = new Bundle();
        if (e instanceof UnknownHostException) {
            resultData.putString(MESSAGE_ERROR_FLAG, "Mohon periksa koneksi internet Anda");
        } else if (e instanceof IOException) {
            resultData.putString(MESSAGE_ERROR_FLAG, "Silahkan coba lagi");
        } else {
            resultData.putString(MESSAGE_ERROR_FLAG, "Silahkan coba lagi");
        }
        resultData.putInt(TYPE, type);
        receiver.send(DownloadService.STATUS_ERROR, resultData);
    }

    @Override
    public void onNext(Response<String> stringResponse) {

        String response = String.valueOf(stringResponse.body());
        Bundle result = new Bundle();
        result.putInt(TYPE, type);
        ErrorModel errorModel = new GsonBuilder().create().fromJson(response, ErrorModel.class);
        if (errorModel.getError() != null) {
            result.putBoolean(DO_LOGIN, false);
            result.putString(MESSAGE_ERROR_FLAG, errorModel.getErrorDescription());
            receiver.send(DownloadService.STATUS_ERROR, result);
        } else {
            switch (type) {
                case LOGIN_ACCOUNTS_TOKEN:
                case LOGIN_GOOGLE:
                case LOGIN_FACEBOOK:
                case LOGIN_WEBVIEW:
                case REGISTER_FACEBOOK:
                    TokenModel model = new GsonBuilder().create().fromJson(response, TokenModel.class);
                    result.putBoolean(DO_LOGIN, true);
                    result.putParcelable(TOKEN_BUNDLE, model);
                    result.putParcelable(EXTRA_TYPE, parcelable);
                    sessionHandler.setToken(model.getAccessToken(), model.getTokenType(), model.getRefreshToken());
                    receiver.send(DownloadService.STATUS_FINISHED, result);
                    break;
                case LOGIN_ACCOUNTS_INFO:
                    InfoModel infoModel = new GsonBuilder().create().fromJson(response, InfoModel.class);
                    result.putBoolean(DO_LOGIN, true);
                    result.putParcelable(INFO_BUNDLE, infoModel);
                    result.putParcelable(EXTRA_TYPE, parcelable);
                    sessionHandler.setTempLoginSession(String.valueOf(infoModel.getUserId()));
                    receiver.send(DownloadService.STATUS_FINISHED, result);
                    break;

//                case LOGIN_ACCOUNTS_PROFILE:
//                    ProfileModel profileModel = new GsonBuilder().create()
//                            .fromJson(response, ProfileModel.class);
//                    result.putParcelable(PROFILE_BUNDLE, profileModel);
//                    receiver.send(DownloadService.STATUS_FINISHED, result);
//                    break;
                default:
                    break;
            }
        }
    }
}
