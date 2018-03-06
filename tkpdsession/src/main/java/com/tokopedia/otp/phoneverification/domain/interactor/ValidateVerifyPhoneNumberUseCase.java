package com.tokopedia.otp.phoneverification.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.otp.data.model.ValidateOtpDomain;
import com.tokopedia.otp.domain.interactor.ValidateOtpUseCase;
import com.tokopedia.otp.phoneverification.data.VerifyPhoneNumberDomain;
import com.tokopedia.otp.phoneverification.data.model.ValidateVerifyPhoneNumberDomain;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * @author by nisie on 10/24/17.
 */

public class ValidateVerifyPhoneNumberUseCase extends UseCase<ValidateVerifyPhoneNumberDomain> {

    private ValidateOtpUseCase validateOtpUseCase;
    private VerifyPhoneNumberUseCase verifyPhoneNumberUseCase;
    private SessionHandler sessionHandler;

    @Inject
    public ValidateVerifyPhoneNumberUseCase(ThreadExecutor threadExecutor,
                                            PostExecutionThread postExecutionThread,
                                            ValidateOtpUseCase validateOtpUseCase,
                                            VerifyPhoneNumberUseCase verifyPhoneNumberUseCase,
                                            SessionHandler sessionHandler) {
        super(threadExecutor, postExecutionThread);
        this.validateOtpUseCase = validateOtpUseCase;
        this.verifyPhoneNumberUseCase = verifyPhoneNumberUseCase;
        this.sessionHandler = sessionHandler;
    }

    @Override
    public Observable<ValidateVerifyPhoneNumberDomain> createObservable(RequestParams requestParams) {
        ValidateVerifyPhoneNumberDomain domain = new ValidateVerifyPhoneNumberDomain();
        return validateOtp(requestParams, domain)
                .flatMap(verifyPhoneNumber(requestParams, domain));
    }

    private Func1<ValidateVerifyPhoneNumberDomain, Observable<ValidateVerifyPhoneNumberDomain>>
    verifyPhoneNumber(final RequestParams requestParams, final ValidateVerifyPhoneNumberDomain domain) {
        return new Func1<ValidateVerifyPhoneNumberDomain, Observable<ValidateVerifyPhoneNumberDomain>>() {
            @Override
            public Observable<ValidateVerifyPhoneNumberDomain> call(ValidateVerifyPhoneNumberDomain validateVerifyPhoneNumberDomain) {
                return verifyPhoneNumberUseCase.createObservable(VerifyPhoneNumberUseCase.getParam(
                        requestParams.getString(VerifyPhoneNumberUseCase.PARAM_USER_ID, ""),
                        requestParams.getString(VerifyPhoneNumberUseCase.PARAM_PHONE, "")))
                        .flatMap(new Func1<VerifyPhoneNumberDomain, Observable<ValidateVerifyPhoneNumberDomain>>() {
                            @Override
                            public Observable<ValidateVerifyPhoneNumberDomain> call(VerifyPhoneNumberDomain verifyPhoneNumberDomain) {
                                verifyPhoneNumberDomain.setPhoneNumber(requestParams.getString
                                        (VerifyPhoneNumberUseCase.PARAM_PHONE, ""));
                                domain.setVerifyPhoneDomain(verifyPhoneNumberDomain);
                                return Observable.just(domain);
                            }
                        })
                        .doOnNext(saveToSession());
            }
        };
    }

    private Action1<ValidateVerifyPhoneNumberDomain> saveToSession() {
        return new Action1<ValidateVerifyPhoneNumberDomain>() {
            @Override
            public void call(ValidateVerifyPhoneNumberDomain validateVerifyPhoneNumberDomain) {
                if (validateVerifyPhoneNumberDomain.getVerifyPhoneDomain().isSuccess()) {
                    sessionHandler.setIsMSISDNVerified(true);
                    sessionHandler.setPhoneNumber(validateVerifyPhoneNumberDomain.getVerifyPhoneDomain().getPhoneNumber());
                }
            }
        };
    }


    private Observable<ValidateVerifyPhoneNumberDomain> validateOtp(RequestParams requestParams,
                                                                    final ValidateVerifyPhoneNumberDomain domain) {
        return validateOtpUseCase.createObservable(
                ValidateOtpUseCase.getParam(
                        requestParams.getString(ValidateOtpUseCase.PARAM_USER, ""),
                        requestParams.getInt(ValidateOtpUseCase.PARAM_OTP_TYPE, -1),
                        requestParams.getString(ValidateOtpUseCase.PARAM_CODE, "")
                ))
                .flatMap(new Func1<ValidateOtpDomain, Observable<ValidateVerifyPhoneNumberDomain>>() {
                    @Override
                    public Observable<ValidateVerifyPhoneNumberDomain> call(ValidateOtpDomain validateOTPDomain) {
                        domain.setValidateOtpDomain(validateOTPDomain);
                        return Observable.just(domain);
                    }
                });
    }

    public static RequestParams getParam(int otpType, String otpCode, String phoneNumber, String
            userId) {
        RequestParams params = RequestParams.create();
        params.putAll(ValidateOtpUseCase.getParam(userId, otpType, otpCode).getParameters());
        params.putAll(VerifyPhoneNumberUseCase.getParam(userId, phoneNumber).getParameters());
        return params;
    }
}
