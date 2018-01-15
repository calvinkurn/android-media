package com.tokopedia.profilecompletion.view.presenter;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.core.network.ErrorMessageException;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.core.network.retrofit.response.ErrorListener;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.otp.data.model.RequestOtpModel;
import com.tokopedia.otp.domainold.RequestOtpUseCase;
import com.tokopedia.otp.domainold.ValidateOtpUseCase;
import com.tokopedia.otp.phoneverification.data.VerifyPhoneNumberModel;
import com.tokopedia.otp.phoneverification.domain.interactor.VerifyPhoneNumberUseCase;
import com.tokopedia.session.R;

import java.net.UnknownHostException;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by nisie on 2/22/17.
 */

public class ProfileCompletionPhoneVerificationPresenter
        extends BaseDaggerPresenter<ProfileCompletionPhoneVerificationContract.View>
        implements ProfileCompletionPhoneVerificationContract.Presenter {

    private static final String OTP_TYPE_PHONE_NUMBER_VERIFICATION = "11";

    private RequestOtpUseCase requestOtpUseCase;
    private ValidateOtpUseCase validateOtpUseCase;
    private VerifyPhoneNumberUseCase verifyPhoneNumberUseCase;


    @Inject
    public ProfileCompletionPhoneVerificationPresenter(
            RequestOtpUseCase requestOtpUseCase,
            ValidateOtpUseCase validateOtpUseCase,
            VerifyPhoneNumberUseCase verifyPhoneNumberUseCase) {
        this.verifyPhoneNumberUseCase = verifyPhoneNumberUseCase;
        this.validateOtpUseCase = validateOtpUseCase;
        this.requestOtpUseCase = requestOtpUseCase;
    }


    @Override
    public void attachView(ProfileCompletionPhoneVerificationContract.View view) {
        super.attachView(view);
    }

    @Override
    public void verifyPhoneNumber() {
        if (isValid()) {
            getView().showProgressDialog();
            getView().setViewEnabled(false);

            verifyPhoneNumberUseCase.execute(getVerifyPhoneNumberParam(),
                    new Subscriber<VerifyPhoneNumberModel>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            if (e instanceof UnknownHostException) {
                                getView().onErrorVerifyPhoneNumber(
                                        getView().getString(R.string.msg_no_connection));
                            } else if (e instanceof RuntimeException &&
                                    e.getLocalizedMessage() != null &&
                                    e.getLocalizedMessage().length() <= 3) {
                                new ErrorHandler(new ErrorListener() {
                                    @Override
                                    public void onUnknown() {
                                        getView().onErrorVerifyPhoneNumber(
                                                getView().getString(R.string.default_request_error_unknown));
                                    }

                                    @Override
                                    public void onTimeout() {
                                        getView().onErrorVerifyPhoneNumber(
                                                getView().getString(R.string.default_request_error_timeout));
                                    }

                                    @Override
                                    public void onServerError() {
                                        getView().onErrorVerifyPhoneNumber(
                                                getView().getString(R.string.default_request_error_internal_server));
                                    }

                                    @Override
                                    public void onBadRequest() {
                                        getView().onErrorVerifyPhoneNumber(
                                                getView().getString(R.string.default_request_error_bad_request));
                                    }

                                    @Override
                                    public void onForbidden() {
                                        getView().onErrorVerifyPhoneNumber(
                                                getView().getString(R.string.default_request_error_forbidden_auth));
                                    }
                                }, Integer.parseInt(e.getLocalizedMessage()));
                            } else if (e instanceof ErrorMessageException
                                    && e.getLocalizedMessage() != null) {
                                getView().onErrorVerifyPhoneNumber(e.getLocalizedMessage());
                            } else {
                                getView().onErrorVerifyPhoneNumber(
                                        getView().getString(R.string.default_request_error_unknown));
                            }
                        }

                        @Override
                        public void onNext(VerifyPhoneNumberModel verifyPhoneNumberModel) {
                            getView().onSuccessVerifyPhoneNumber();

                        }
                    });

        }
    }

    private boolean isValid() {
        boolean isValid = true;
        if (getView().getPhoneNumber().length() == 0) {
            getView().showErrorPhoneNumber(getView().getString(R.string.error_field_required));
            isValid = false;
        }
        return isValid;
    }

    @Override
    public void requestOtp() {
        if (isValid()) {
            getView().setViewEnabled(false);
            getView().showProgressDialog();

            requestOtpUseCase.execute(getRequestOTPParam(), new Subscriber<RequestOtpModel>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            if (e instanceof UnknownHostException) {
                                getView().onErrorRequestOTP(
                                        getView().getString(R.string.msg_no_connection));
                            } else if (e instanceof RuntimeException &&
                                    e.getLocalizedMessage() != null &&
                                    e.getLocalizedMessage().length() <= 3) {
                                new ErrorHandler(new ErrorListener() {
                                    @Override
                                    public void onUnknown() {
                                        getView().onErrorRequestOTP(
                                                getView().getString(R.string.default_request_error_unknown));
                                    }

                                    @Override
                                    public void onTimeout() {
                                        getView().onErrorRequestOTP(
                                                getView().getString(R.string.default_request_error_timeout));
                                    }

                                    @Override
                                    public void onServerError() {
                                        getView().onErrorRequestOTP(
                                                getView().getString(R.string.default_request_error_internal_server));
                                    }

                                    @Override
                                    public void onBadRequest() {
                                        getView().onErrorRequestOTP(
                                                getView().getString(R.string.default_request_error_bad_request));
                                    }

                                    @Override
                                    public void onForbidden() {
                                        getView().onErrorRequestOTP(
                                                getView().getString(R.string.default_request_error_forbidden_auth));
                                    }
                                }, Integer.parseInt(e.getLocalizedMessage()));
                            } else if (e instanceof ErrorMessageException
                                    && e.getLocalizedMessage() != null) {
                                getView().onErrorRequestOTP(e.getLocalizedMessage());
                            } else {
                                getView().onErrorRequestOTP(
                                        getView().getString(R.string.default_request_error_unknown));
                            }
                        }

                        @Override
                        public void onNext(RequestOtpModel requestOtpModel) {
                            getView().onSuccessRequestOtp(requestOtpModel.getStatusMessage());
                        }

                    }

            );
        }
    }

    @Override
    public void requestOtpWithCall() {
        if (isValid()) {
            getView().setViewEnabled(false);
            getView().showProgressDialog();

            requestOtpUseCase.execute(getRequestOTPWithCallParam(), new Subscriber<RequestOtpModel>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    if (e instanceof UnknownHostException) {
                        getView().onErrorRequestOTP(
                                getView().getString(R.string.msg_no_connection));
                    } else if (e instanceof RuntimeException
                            && e.getLocalizedMessage() != null
                            && e.getLocalizedMessage().length() <= 3) {
                        new ErrorHandler(new ErrorListener() {
                            @Override
                            public void onUnknown() {
                                getView().onErrorRequestOTP(
                                        getView().getString(R.string.default_request_error_unknown));
                            }

                            @Override
                            public void onTimeout() {
                                getView().onErrorRequestOTP(
                                        getView().getString(R.string.default_request_error_timeout));
                            }

                            @Override
                            public void onServerError() {
                                getView().onErrorRequestOTP(
                                        getView().getString(R.string.default_request_error_internal_server));
                            }

                            @Override
                            public void onBadRequest() {
                                getView().onErrorRequestOTP(
                                        getView().getString(R.string.default_request_error_bad_request));
                            }

                            @Override
                            public void onForbidden() {
                                getView().onErrorRequestOTP(
                                        getView().getString(R.string.default_request_error_forbidden_auth));
                            }
                        }, Integer.parseInt(e.getLocalizedMessage()));
                    } else if (e instanceof ErrorMessageException
                            && e.getLocalizedMessage() != null) {
                        getView().onErrorRequestOTP(e.getLocalizedMessage());
                    } else {
                        getView().onErrorRequestOTP(
                                getView().getString(R.string.default_request_error_unknown));
                    }
                }

                @Override
                public void onNext(RequestOtpModel requestOtpModel) {

                    getView().onSuccessRequestOtp(requestOtpModel.getStatusMessage());

                }
            });
        }
    }

    @Override
    public void onDestroyView() {
        requestOtpUseCase.unsubscribe();
        validateOtpUseCase.unsubscribe();
        verifyPhoneNumberUseCase.unsubscribe();
    }

    private RequestParams getRequestOTPParam() {
        RequestParams param = RequestParams.create();
        param.putString(RequestOtpUseCase.PARAM_MODE, RequestOtpUseCase.MODE_SMS);
        param.putString(RequestOtpUseCase.PARAM_OTP_TYPE, OTP_TYPE_PHONE_NUMBER_VERIFICATION);
        param.putString(RequestOtpUseCase.PARAM_MSISDN, getView().getPhoneNumber());
        return param;
    }

    private RequestParams getRequestOTPWithCallParam() {
        RequestParams param = RequestParams.create();
        param.putString(RequestOtpUseCase.PARAM_MODE, RequestOtpUseCase.MODE_CALL);
        param.putString(RequestOtpUseCase.PARAM_OTP_TYPE, OTP_TYPE_PHONE_NUMBER_VERIFICATION);
        param.putString(RequestOtpUseCase.PARAM_MSISDN, getView().getPhoneNumber());
        return param;
    }

    private RequestParams getVerifyPhoneNumberParam() {
        RequestParams param = RequestParams.create();
        setValidateOtpParam(param);
        setVerifyOtpParam(param);
        return param;
    }

    private void setValidateOtpParam(RequestParams param) {
        param.putString(ValidateOtpUseCase.PARAM_CODE, getView().getOTPCode());
        param.putString(ValidateOtpUseCase.PARAM_USER, SessionHandler.getLoginID(getView().getActivity()));
    }

    private void setVerifyOtpParam(RequestParams param) {
        param.putString(VerifyPhoneNumberUseCase.PARAM_USER_ID, SessionHandler.getLoginID(getView().getActivity()));
        param.putString(VerifyPhoneNumberUseCase.PARAM_PHONE, getView().getPhoneNumber());
    }


}
