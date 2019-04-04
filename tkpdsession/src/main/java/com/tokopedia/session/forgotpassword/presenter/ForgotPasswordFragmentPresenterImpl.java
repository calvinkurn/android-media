package com.tokopedia.session.forgotpassword.presenter;

import android.content.Context;
import android.util.Log;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.analytics.nishikino.model.EventTracking;
import com.tokopedia.network.ErrorHandler;
import com.tokopedia.core.network.retrofit.response.ErrorListener;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.core.rxjava.RxUtils;
import com.tokopedia.core.service.constant.DownloadServiceConstant;
import com.tokopedia.session.R;
import com.tokopedia.session.forgotpassword.interactor.ForgotPasswordRetrofitInteractor;
import com.tokopedia.session.forgotpassword.listener.ForgotPasswordFragmentView;
import com.tokopedia.track.TrackApp;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import retrofit2.Response;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Alifa on 10/17/2016.
 */

public class ForgotPasswordFragmentPresenterImpl implements ForgotPasswordFragmentPresenter {

    public static final int REQUEST_FORGOT_PASSWORD_CODE = 1;
    private static final String TOO_MANY_REQUEST = "TOO_MANY_REQUEST";
    private static final String TAG = ForgotPasswordFragmentPresenterImpl.class.getSimpleName();
    private final CompositeSubscription compositeSubscription;
    ForgotPasswordFragmentView viewListener;
    ForgotPasswordRetrofitInteractor networkInteractor;

    public ForgotPasswordFragmentPresenterImpl(ForgotPasswordFragmentView viewListener,
                                               ForgotPasswordRetrofitInteractor networkInteractor,
                                               CompositeSubscription compositeSubscription) {
        this.viewListener = viewListener;
        this.networkInteractor = networkInteractor;
        this.compositeSubscription = compositeSubscription;
    }


    @Override
    public void initData(String o) {

    }

    @Override
    public void onDestroyView() {
        RxUtils.unsubscribeIfNotNull(compositeSubscription);
    }

    @Override
    public void resetPassword() {
        viewListener.resetError();
        if (isValidForm()) {
            viewListener.showLoadingProgress();
            eventForgotPassword();

            compositeSubscription.add(networkInteractor.resetPassword(getResetPasswordParam())
                    .subscribeOn(Schedulers.newThread())
                    .unsubscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<Response<TkpdResponse>>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            String error;
                            if (e instanceof UnknownHostException) {
                                viewListener.onErrorResetPassword(viewListener.getString(R.string.msg_no_connection));
                            } else if (e instanceof SocketTimeoutException) {
                                viewListener.onErrorResetPassword(viewListener.getString(R.string.default_request_error_timeout));
                            } else if (e instanceof IOException) {
                                viewListener.onErrorResetPassword(viewListener.getString(R.string.default_request_error_internal_server));
                            } else {
                                viewListener.onErrorResetPassword(viewListener.getString(R.string.default_request_error_unknown));
                            }

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
                                if (response.getStatus().equals(TOO_MANY_REQUEST)) {
                                } else if (!response.isError() && jsonObject.optInt("is_success", 0) == 1) {
                                    viewListener.onSuccessResetPassword();
                                } else {
                                    viewListener.onErrorResetPassword(response.getErrorMessageJoined());
                                }
                            } else {
                                new ErrorHandler(new ErrorListener() {
                                    @Override
                                    public void onUnknown() {
                                        viewListener.onErrorResetPassword(viewListener.getString(R.string.default_request_error_unknown));

                                    }

                                    @Override
                                    public void onTimeout() {
                                        viewListener.onErrorResetPassword(viewListener.getString(R.string.default_request_error_timeout));

                                    }

                                    @Override
                                    public void onServerError() {
                                        viewListener.onErrorResetPassword(viewListener.getString(R.string.default_request_error_internal_server));

                                    }

                                    @Override
                                    public void onBadRequest() {
                                        viewListener.onErrorResetPassword(viewListener.getString(R.string.default_request_error_bad_request));

                                    }

                                    @Override
                                    public void onForbidden() {
                                        viewListener.onErrorResetPassword(viewListener.getString(R.string.default_request_error_forbidden_auth));

                                    }
                                }, responseData.code());
                            }
                        }
                    }));
        }
    }

    public void eventForgotPassword() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(
                AppEventTracking.Event.FORGOT_PASSWORD,
                AppEventTracking.Category.FORGOT_PASSWORD,
                AppEventTracking.Action.RESET_SUCCESS,
                AppEventTracking.EventLabel.RESET_PASSWORD);
    }

    private boolean isValidForm() {
        Boolean isValid = true;

        if (viewListener.getEmail().getText().toString().length() == 0) {
            viewListener.setEmailError(viewListener.getString(com.tokopedia.core2.R.string.error_field_required));
            isValid = false;

        } else if (!CommonUtils.EmailValidation(viewListener.getEmail().getText().toString())) {
            viewListener.setEmailError(viewListener.getString(com.tokopedia.core2.R.string.error_invalid_email));
            isValid = false;
        }

        return isValid;
    }


    public TKPDMapParam<String, String> getResetPasswordParam() {
        TKPDMapParam<String, String> param = new TKPDMapParam<>();
        param.put("email", viewListener.getEmail().getText().toString());
        return param;
    }
}
