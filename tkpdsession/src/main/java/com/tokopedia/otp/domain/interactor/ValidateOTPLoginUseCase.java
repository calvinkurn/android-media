package com.tokopedia.otp.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.otp.data.model.ValidateOTPDomain;
import com.tokopedia.otp.data.model.ValidateOTPLoginDomain;
import com.tokopedia.session.data.viewmodel.login.MakeLoginDomain;
import com.tokopedia.session.domain.interactor.MakeLoginUseCase;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * @author by nisie on 10/23/17.
 */

public class ValidateOTPLoginUseCase extends UseCase<ValidateOTPLoginDomain> {

    ValidateOtpUseCase validateOTPUseCase;
    MakeLoginUseCase makeLoginUseCase;

    @Inject
    public ValidateOTPLoginUseCase(ThreadExecutor threadExecutor,
                                   PostExecutionThread postExecutionThread,
                                   ValidateOtpUseCase validateOTPUseCase,
                                   MakeLoginUseCase makeLoginUseCase) {
        super(threadExecutor, postExecutionThread);
        this.validateOTPUseCase = validateOTPUseCase;
        this.makeLoginUseCase = makeLoginUseCase;
    }

    @Override
    public Observable<ValidateOTPLoginDomain> createObservable(RequestParams requestParams) {
        ValidateOTPLoginDomain domain = new ValidateOTPLoginDomain();
        return validateOTP(requestParams, domain)
                .flatMap(makeLogin(requestParams, domain));
    }

    private Observable<ValidateOTPLoginDomain> validateOTP(RequestParams requestParams,
                                                           final ValidateOTPLoginDomain domain) {
        return validateOTPUseCase.createObservable(ValidateOtpUseCase.getParam(
                requestParams.getInt(ValidateOtpUseCase.PARAM_OTP_TYPE, -1),
                requestParams.getString(ValidateOtpUseCase.PARAM_CODE, ""),
                requestParams.getString(ValidateOtpUseCase.PARAM_USER, "")
        )).flatMap(new Func1<ValidateOTPDomain, Observable<ValidateOTPLoginDomain>>() {
            @Override
            public Observable<ValidateOTPLoginDomain> call(ValidateOTPDomain validateOTPDomain) {
                if (validateOTPDomain.isSuccess())
                    domain.setValidateOTPDomain(validateOTPDomain);
                return Observable.just(domain);
            }
        });
    }

    private Func1<ValidateOTPLoginDomain, Observable<ValidateOTPLoginDomain>> makeLogin(final RequestParams requestParams,
                                                                                        final ValidateOTPLoginDomain domain) {
        return new Func1<ValidateOTPLoginDomain, Observable<ValidateOTPLoginDomain>>() {
            @Override
            public Observable<ValidateOTPLoginDomain> call(ValidateOTPLoginDomain validateOTPLoginDomain) {
                return makeLoginUseCase.createObservable(MakeLoginUseCase.getParam(
                        requestParams.getString(MakeLoginUseCase.PARAM_USER_ID, "")
                ))
                        .flatMap(new Func1<MakeLoginDomain, Observable<ValidateOTPLoginDomain>>() {
                            @Override
                            public Observable<ValidateOTPLoginDomain> call(MakeLoginDomain makeLoginDomain) {
                                domain.setMakeLoginDomain(makeLoginDomain);
                                return Observable.just(domain);
                            }
                        });
            }
        };
    }

    public static RequestParams getParam(int otpType, String otp, String tempUserId) {
        RequestParams params = RequestParams.create();
        params.putAll(ValidateOtpUseCase.getParam(otpType, otp, tempUserId).getParameters());
        params.putString(MakeLoginUseCase.PARAM_USER_ID, tempUserId);
        return params;
    }
}
