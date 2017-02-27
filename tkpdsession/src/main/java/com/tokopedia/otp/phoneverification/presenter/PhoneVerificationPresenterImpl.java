package com.tokopedia.otp.phoneverification.presenter;

import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.core.network.retrofit.response.ErrorListener;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.core.rxjava.RxUtils;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.otp.phoneverification.interactor.PhoneVerificationNetworkInteractorImpl;
import com.tokopedia.otp.phoneverification.listener.PhoneVerificationFragmentView;
import com.tokopedia.session.R;

import org.json.JSONException;

import java.net.UnknownHostException;

import retrofit2.Response;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by nisie on 2/22/17.
 */

public class PhoneVerificationPresenterImpl implements PhoneVerificationPresenter {

    private static final String OTP_TYPE_PHONE_NUMBER_VERIFICATION = "11";

    private final PhoneVerificationFragmentView viewListener;
    private final PhoneVerificationNetworkInteractorImpl networkInteractor;
    private final CompositeSubscription compositeSubscription;

    public PhoneVerificationPresenterImpl(PhoneVerificationFragmentView viewListener,
                                          CompositeSubscription compositeSubscription,
                                          PhoneVerificationNetworkInteractorImpl networkInteractor) {
        this.viewListener = viewListener;
        this.compositeSubscription = compositeSubscription;
        this.networkInteractor = networkInteractor;
    }

    @Override
    public void verifyPhoneNumber() {
        if (isValid())
            compositeSubscription.add(networkInteractor.verifyPhoneNumber(getVerifyOTPParam())
                    .subscribeOn(Schedulers.newThread())
                    .unsubscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<Response<TkpdResponse>>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            if (e instanceof UnknownHostException) {
                                viewListener.onErrorRequestOTP(
                                        viewListener.getString(R.string.msg_no_connection));
                            } else if (e instanceof RuntimeException &&
                                    e.getLocalizedMessage() != null &&
                                    e.getLocalizedMessage().length() <= 3) {
                                new ErrorHandler(new ErrorListener() {
                                    @Override
                                    public void onUnknown() {
                                        viewListener.onErrorVerifyOTP(
                                                viewListener.getString(R.string.default_request_error_unknown));
                                    }

                                    @Override
                                    public void onTimeout() {
                                        viewListener.onErrorVerifyOTP(
                                                viewListener.getString(R.string.default_request_error_timeout));
                                    }

                                    @Override
                                    public void onServerError() {
                                        viewListener.onErrorVerifyOTP(
                                                viewListener.getString(R.string.default_request_error_internal_server));
                                    }

                                    @Override
                                    public void onBadRequest() {
                                        viewListener.onErrorVerifyOTP(
                                                viewListener.getString(R.string.default_request_error_bad_request));
                                    }

                                    @Override
                                    public void onForbidden() {
                                        viewListener.onErrorVerifyOTP(
                                                viewListener.getString(R.string.default_request_error_forbidden_auth));
                                    }
                                }, Integer.parseInt(e.getLocalizedMessage()));
                            } else if (e instanceof RuntimeException && e.getLocalizedMessage() != null) {
                                viewListener.onErrorVerifyOTP(e.getLocalizedMessage());
                            } else {
                                viewListener.onErrorVerifyOTP(
                                        viewListener.getString(R.string.default_request_error_unknown));
                            }
                        }

                        @Override
                        public void onNext(Response<TkpdResponse> responseData) {
                            if (responseData.isSuccessful()) {
                                try {
                                    if (responseData.body().getJsonData().getString("is_verified").equals("")
                                            && !responseData.body().getStatusMessageJoined().equals("")) {
                                        viewListener.onSuccessVerifyOTP();
                                    } else {
                                        viewListener.onErrorVerifyOTP(responseData.body().getErrorMessageJoined());
                                    }
                                } catch (JSONException e) {
                                    viewListener.onErrorVerifyOTP(responseData.body().getErrorMessageJoined());
                                }
                            } else {
                                new ErrorHandler(new ErrorListener() {
                                    @Override
                                    public void onUnknown() {
                                        viewListener.onErrorVerifyOTP(viewListener.getString(R.string.default_request_error_unknown));
                                    }

                                    @Override
                                    public void onTimeout() {
                                        viewListener.onErrorVerifyOTP(viewListener.getString(R.string.default_request_error_timeout));
                                    }

                                    @Override
                                    public void onServerError() {
                                        viewListener.onErrorVerifyOTP(viewListener.getString(R.string.default_request_error_internal_server));
                                    }

                                    @Override
                                    public void onBadRequest() {
                                        viewListener.onErrorVerifyOTP(viewListener.getString(R.string.default_request_error_bad_request));
                                    }

                                    @Override
                                    public void onForbidden() {
                                        viewListener.onErrorVerifyOTP(viewListener.getString(R.string.default_request_error_forbidden_auth));
                                    }
                                }, responseData.code());
                            }

                        }
                    }));
    }

    private boolean isValid() {
        boolean isValid = true;
        if (viewListener.getPhoneNumber().length() == 0) {
            viewListener.showErrorPhoneNumber(viewListener.getString(R.string.error_field_required));
            isValid = false;
        }
        return isValid;
    }

    @Override
    public void requestOtp() {
        viewListener.showProgressDialog();
        compositeSubscription.add(networkInteractor.requestOTP(
                SessionHandler.getLoginID(viewListener.getActivity()), getRequestOTPParam())
                .subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Response<TkpdResponse>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof UnknownHostException) {
                            viewListener.onErrorRequestOTP(
                                    viewListener.getString(R.string.msg_no_connection));
                        } else if (e instanceof RuntimeException &&
                                e.getLocalizedMessage() != null &&
                                e.getLocalizedMessage().length() <= 3) {
                            new ErrorHandler(new ErrorListener() {
                                @Override
                                public void onUnknown() {
                                    viewListener.onErrorRequestOTP(
                                            viewListener.getString(R.string.default_request_error_unknown));
                                }

                                @Override
                                public void onTimeout() {
                                    viewListener.onErrorRequestOTP(
                                            viewListener.getString(R.string.default_request_error_timeout));
                                }

                                @Override
                                public void onServerError() {
                                    viewListener.onErrorRequestOTP(
                                            viewListener.getString(R.string.default_request_error_internal_server));
                                }

                                @Override
                                public void onBadRequest() {
                                    viewListener.onErrorRequestOTP(
                                            viewListener.getString(R.string.default_request_error_bad_request));
                                }

                                @Override
                                public void onForbidden() {
                                    viewListener.onErrorRequestOTP(
                                            viewListener.getString(R.string.default_request_error_forbidden_auth));
                                }
                            }, Integer.parseInt(e.getLocalizedMessage()));
                        } else if (e instanceof RuntimeException && e.getLocalizedMessage() != null) {
                            viewListener.onErrorRequestOTP(e.getLocalizedMessage());
                        } else {
                            viewListener.onErrorRequestOTP(
                                    viewListener.getString(R.string.default_request_error_unknown));
                        }
                    }

                    @Override
                    public void onNext(Response<TkpdResponse> responseData) {
                        if (responseData.isSuccessful()) {
                            if (responseData.body().getErrorMessageJoined().equals("")
                                    && !responseData.body().getStatusMessageJoined().equals("")) {
                                viewListener.onSuccessRequestOtp(responseData.body().getStatusMessageJoined());
                            } else {
                                viewListener.onErrorRequestOTP(responseData.body().getErrorMessageJoined());
                            }
                        } else {
                            new ErrorHandler(new ErrorListener() {
                                @Override
                                public void onUnknown() {
                                    viewListener.onErrorRequestOTP(viewListener.getString(R.string.default_request_error_unknown));
                                }

                                @Override
                                public void onTimeout() {
                                    viewListener.onErrorRequestOTP(viewListener.getString(R.string.default_request_error_timeout));
                                }

                                @Override
                                public void onServerError() {
                                    viewListener.onErrorRequestOTP(viewListener.getString(R.string.default_request_error_internal_server));
                                }

                                @Override
                                public void onBadRequest() {
                                    viewListener.onErrorRequestOTP(viewListener.getString(R.string.default_request_error_bad_request));
                                }

                                @Override
                                public void onForbidden() {
                                    viewListener.onErrorRequestOTP(viewListener.getString(R.string.default_request_error_forbidden_auth));
                                }
                            }, responseData.code());
                        }

                    }
                }));
    }

    @Override
    public void requestOtpWithCall() {
        viewListener.showProgressDialog();
        compositeSubscription.add(networkInteractor.requestOTP(
                SessionHandler.getLoginID(viewListener.getActivity()), getRequestOTPWithCallParam())
                .subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Response<TkpdResponse>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof UnknownHostException) {
                            viewListener.onErrorRequestOTP(
                                    viewListener.getString(R.string.msg_no_connection));
                        } else if (e instanceof RuntimeException &&
                                e.getLocalizedMessage() != null &&
                                e.getLocalizedMessage().length() <= 3) {
                            new ErrorHandler(new ErrorListener() {
                                @Override
                                public void onUnknown() {
                                    viewListener.onErrorRequestOTP(
                                            viewListener.getString(R.string.default_request_error_unknown));
                                }

                                @Override
                                public void onTimeout() {
                                    viewListener.onErrorRequestOTP(
                                            viewListener.getString(R.string.default_request_error_timeout));
                                }

                                @Override
                                public void onServerError() {
                                    viewListener.onErrorRequestOTP(
                                            viewListener.getString(R.string.default_request_error_internal_server));
                                }

                                @Override
                                public void onBadRequest() {
                                    viewListener.onErrorRequestOTP(
                                            viewListener.getString(R.string.default_request_error_bad_request));
                                }

                                @Override
                                public void onForbidden() {
                                    viewListener.onErrorRequestOTP(
                                            viewListener.getString(R.string.default_request_error_forbidden_auth));
                                }
                            }, Integer.parseInt(e.getLocalizedMessage()));
                        } else if (e instanceof RuntimeException && e.getLocalizedMessage() != null) {
                            viewListener.onErrorRequestOTP(e.getLocalizedMessage());
                        } else {
                            viewListener.onErrorRequestOTP(
                                    viewListener.getString(R.string.default_request_error_unknown));
                        }
                    }

                    @Override
                    public void onNext(Response<TkpdResponse> responseData) {
                        if (responseData.isSuccessful()) {
                            if (responseData.body().getErrorMessageJoined().equals("")
                                    && !responseData.body().getStatusMessageJoined().equals("")) {
                                viewListener.onSuccessRequestOtp(responseData.body().getStatusMessageJoined());
                            } else {
                                viewListener.onErrorRequestOTP(responseData.body().getErrorMessageJoined());
                            }
                        } else {
                            new ErrorHandler(new ErrorListener() {
                                @Override
                                public void onUnknown() {
                                    viewListener.onErrorRequestOTP(viewListener.getString(R.string.default_request_error_unknown));
                                }

                                @Override
                                public void onTimeout() {
                                    viewListener.onErrorRequestOTP(viewListener.getString(R.string.default_request_error_timeout));
                                }

                                @Override
                                public void onServerError() {
                                    viewListener.onErrorRequestOTP(viewListener.getString(R.string.default_request_error_internal_server));
                                }

                                @Override
                                public void onBadRequest() {
                                    viewListener.onErrorRequestOTP(viewListener.getString(R.string.default_request_error_bad_request));
                                }

                                @Override
                                public void onForbidden() {
                                    viewListener.onErrorRequestOTP(viewListener.getString(R.string.default_request_error_forbidden_auth));
                                }
                            }, responseData.code());
                        }

                    }
                }));
    }

    @Override
    public void onDestroyView() {
        RxUtils.unsubscribeIfNotNull(compositeSubscription);
    }

    private TKPDMapParam<String, String> getRequestOTPParam() {
        TKPDMapParam param = new TKPDMapParam();
        param.put("mode", "sms");
        param.put("otp_type", OTP_TYPE_PHONE_NUMBER_VERIFICATION);
        param.put("msisdn", viewListener.getPhoneNumber());
        return param;
    }

    public TKPDMapParam<String, String> getRequestOTPWithCallParam() {
        TKPDMapParam param = new TKPDMapParam();
        param.put("mode", "call");
        param.put("otp_type", OTP_TYPE_PHONE_NUMBER_VERIFICATION);
        param.put("msisdn", viewListener.getPhoneNumber());
        return param;
    }

    public TKPDMapParam<String, String> getVerifyOTPParam() {
        TKPDMapParam param = new TKPDMapParam();
        param.put("otp", viewListener.getOTPCode());
        param.put("phone", viewListener.getPhoneNumber());
        return param;
    }
}
