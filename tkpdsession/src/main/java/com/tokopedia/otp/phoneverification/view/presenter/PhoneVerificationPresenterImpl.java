package com.tokopedia.otp.phoneverification.view.presenter;

import android.os.Bundle;

import com.tokopedia.core.base.data.executor.JobExecutor;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.UIThread;
import com.tokopedia.core.network.ErrorMessageException;
import com.tokopedia.core.network.apiservices.accounts.AccountsService;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.core.network.retrofit.response.ErrorListener;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.otp.data.factory.OtpSourceFactory;
import com.tokopedia.otp.data.model.RequestOtpModel;
import com.tokopedia.otp.data.repository.OtpRepositoryImpl;
import com.tokopedia.otp.domainold.RequestOtpUseCase;
import com.tokopedia.otp.domainold.ValidateOtpUseCase;
import com.tokopedia.otp.phoneverification.data.VerifyPhoneNumberModel;
import com.tokopedia.otp.phoneverification.data.factory.MsisdnSourceFactory;
import com.tokopedia.otp.phoneverification.data.mapper.ChangePhoneNumberMapper;
import com.tokopedia.otp.phoneverification.data.mapper.VerifyPhoneNumberMapper;
import com.tokopedia.otp.phoneverification.data.repository.MsisdnRepositoryImpl;
import com.tokopedia.otp.phoneverification.domain.interactor.VerifyPhoneNumberUseCase;
import com.tokopedia.otp.phoneverification.view.listener.PhoneVerificationFragmentView;
import com.tokopedia.session.R;

import java.net.UnknownHostException;

import rx.Subscriber;

/**
 * Created by nisie on 2/22/17.
 */

public class PhoneVerificationPresenterImpl implements PhoneVerificationPresenter {

    private static final String OTP_TYPE_PHONE_NUMBER_VERIFICATION = "11";
    private static final String TOKEN_BEARER = "Bearer ";

    private final PhoneVerificationFragmentView viewListener;
    private RequestOtpUseCase requestOtpUseCase;
    private ValidateOtpUseCase validateOtpUseCase;
    private VerifyPhoneNumberUseCase verifyPhoneNumberUseCase;


    public PhoneVerificationPresenterImpl(PhoneVerificationFragmentView viewListener) {
        this.viewListener = viewListener;

        Bundle bundle = new Bundle();
        SessionHandler sessionHandler = new SessionHandler(viewListener.getActivity());
        bundle.putString(AccountsService.AUTH_KEY,
                TOKEN_BEARER + sessionHandler.getAccessToken(viewListener.getActivity()));
        bundle.putBoolean(AccountsService.USING_BOTH_AUTHORIZATION,
                true);
        AccountsService accountsService = new AccountsService(bundle);
        OtpRepositoryImpl otpRepository = new OtpRepositoryImpl(
                new OtpSourceFactory(viewListener.getActivity()));
        MsisdnRepositoryImpl msisdnRepository = new MsisdnRepositoryImpl(
                new MsisdnSourceFactory(viewListener.getActivity(),
                        accountsService,
                        new VerifyPhoneNumberMapper(),
                        new ChangePhoneNumberMapper()));
        this.requestOtpUseCase = new RequestOtpUseCase(new JobExecutor(),
                new UIThread(), otpRepository);
        this.validateOtpUseCase = new ValidateOtpUseCase(new JobExecutor(),
                new UIThread(), otpRepository);
        this.verifyPhoneNumberUseCase = new VerifyPhoneNumberUseCase(new JobExecutor(),
                new UIThread(), msisdnRepository, validateOtpUseCase);
    }

    @Override
    public void verifyPhoneNumber() {
        if (isValid()) {
            viewListener.showProgressDialog();
            viewListener.setViewEnabled(false);

            verifyPhoneNumberUseCase.execute(getVerifyPhoneNumberParam(),
                    new Subscriber<VerifyPhoneNumberModel>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            if (e instanceof UnknownHostException) {
                                viewListener.onErrorVerifyPhoneNumber(
                                        viewListener.getString(R.string.msg_no_connection));
                            } else if (e instanceof RuntimeException &&
                                    e.getLocalizedMessage() != null &&
                                    e.getLocalizedMessage().length() <= 3) {
                                new ErrorHandler(new ErrorListener() {
                                    @Override
                                    public void onUnknown() {
                                        viewListener.onErrorVerifyPhoneNumber(
                                                viewListener.getString(R.string.default_request_error_unknown));
                                    }

                                    @Override
                                    public void onTimeout() {
                                        viewListener.onErrorVerifyPhoneNumber(
                                                viewListener.getString(R.string.default_request_error_timeout));
                                    }

                                    @Override
                                    public void onServerError() {
                                        viewListener.onErrorVerifyPhoneNumber(
                                                viewListener.getString(R.string.default_request_error_internal_server));
                                    }

                                    @Override
                                    public void onBadRequest() {
                                        viewListener.onErrorVerifyPhoneNumber(
                                                viewListener.getString(R.string.default_request_error_bad_request));
                                    }

                                    @Override
                                    public void onForbidden() {
                                        viewListener.onErrorVerifyPhoneNumber(
                                                viewListener.getString(R.string.default_request_error_forbidden_auth));
                                    }
                                }, Integer.parseInt(e.getLocalizedMessage()));
                            } else if (e instanceof ErrorMessageException
                                    && e.getLocalizedMessage() != null) {
                                viewListener.onErrorVerifyPhoneNumber(e.getLocalizedMessage());
                            } else {
                                viewListener.onErrorVerifyPhoneNumber(
                                        viewListener.getString(R.string.default_request_error_unknown));
                            }
                        }

                        @Override
                        public void onNext(VerifyPhoneNumberModel verifyPhoneNumberModel) {
                            viewListener.onSuccessVerifyPhoneNumber();

                        }
                    });

        }
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
        if (isValid()) {
            viewListener.setViewEnabled(false);
            viewListener.showProgressDialog();

            requestOtpUseCase.execute(getRequestOTPParam(), new Subscriber<RequestOtpModel>() {
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
                            } else if (e instanceof ErrorMessageException
                                    && e.getLocalizedMessage() != null) {
                                viewListener.onErrorRequestOTP(e.getLocalizedMessage());
                            } else {
                                viewListener.onErrorRequestOTP(
                                        viewListener.getString(R.string.default_request_error_unknown));
                            }
                        }

                        @Override
                        public void onNext(RequestOtpModel requestOtpModel) {
                            viewListener.onSuccessRequestOtp(requestOtpModel.getStatusMessage());
                        }

                    }

            );
        }
    }

    @Override
    public void requestOtpWithCall() {
        if (isValid()) {
            viewListener.setViewEnabled(false);
            viewListener.showProgressDialog();

            requestOtpUseCase.execute(getRequestOTPWithCallParam(), new Subscriber<RequestOtpModel>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    if (e instanceof UnknownHostException) {
                        viewListener.onErrorRequestOTP(
                                viewListener.getString(R.string.msg_no_connection));
                    } else if (e instanceof RuntimeException
                            && e.getLocalizedMessage() != null
                            && e.getLocalizedMessage().length() <= 3) {
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
                    } else if (e instanceof ErrorMessageException
                            && e.getLocalizedMessage() != null) {
                        viewListener.onErrorRequestOTP(e.getLocalizedMessage());
                    } else {
                        viewListener.onErrorRequestOTP(
                                viewListener.getString(R.string.default_request_error_unknown));
                    }
                }

                @Override
                public void onNext(RequestOtpModel requestOtpModel) {

                    viewListener.onSuccessRequestOtp(requestOtpModel.getStatusMessage());

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
        param.putString(RequestOtpUseCase.PARAM_MSISDN, viewListener.getPhoneNumber());
        return param;
    }

    private RequestParams getRequestOTPWithCallParam() {
        RequestParams param = RequestParams.create();
        param.putString(RequestOtpUseCase.PARAM_MODE, RequestOtpUseCase.MODE_CALL);
        param.putString(RequestOtpUseCase.PARAM_OTP_TYPE, OTP_TYPE_PHONE_NUMBER_VERIFICATION);
        param.putString(RequestOtpUseCase.PARAM_MSISDN, viewListener.getPhoneNumber());
        return param;
    }

    private RequestParams getVerifyPhoneNumberParam() {
        RequestParams param = RequestParams.create();
        setValidateOtpParam(param);
        setVerifyOtpParam(param);
        return param;
    }

    private void setValidateOtpParam(RequestParams param) {
        param.putString(ValidateOtpUseCase.PARAM_CODE, viewListener.getOTPCode());
        param.putString(ValidateOtpUseCase.PARAM_USER, SessionHandler.getLoginID(viewListener.getActivity()));
    }

    private void setVerifyOtpParam(RequestParams param) {
        param.putString(VerifyPhoneNumberUseCase.PARAM_USER_ID, SessionHandler.getLoginID(viewListener.getActivity()));
        param.putString(VerifyPhoneNumberUseCase.PARAM_PHONE, viewListener.getPhoneNumber());
    }

}
