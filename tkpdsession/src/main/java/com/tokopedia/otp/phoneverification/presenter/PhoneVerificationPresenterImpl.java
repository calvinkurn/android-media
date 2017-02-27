package com.tokopedia.otp.phoneverification.presenter;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.core.network.retrofit.response.ErrorListener;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.core.rxjava.RxUtils;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.otp.phoneverification.fragment.PhoneVerificationFragment;
import com.tokopedia.otp.phoneverification.interactor.PhoneVerificationNetworkInteractorImpl;
import com.tokopedia.otp.phoneverification.listener.PhoneVerificationFragmentView;
import com.tokopedia.session.R;
import com.tokopedia.session.register.model.gson.RegisterResult;

import java.net.UnknownHostException;

import retrofit2.Response;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

import static android.R.attr.action;

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
    public void verifyOtp() {
        viewListener.onSuccessVerifyOTP();
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
}
