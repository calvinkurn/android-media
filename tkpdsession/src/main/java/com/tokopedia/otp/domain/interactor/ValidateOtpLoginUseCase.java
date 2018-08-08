package com.tokopedia.otp.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.otp.data.model.ValidateOtpDomain;
import com.tokopedia.otp.data.model.ValidateOtpLoginDomain;
import com.tokopedia.session.data.viewmodel.login.MakeLoginDomain;
import com.tokopedia.session.domain.interactor.MakeLoginUseCase;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * @author by nisie on 10/23/17.
 */

public class ValidateOtpLoginUseCase extends UseCase<ValidateOtpLoginDomain> {

    private final SessionHandler sessionHandler;
    ValidateOtpUseCase validateOtpUseCase;
    MakeLoginUseCase makeLoginUseCase;

    @Inject
    public ValidateOtpLoginUseCase(ThreadExecutor threadExecutor,
                                   PostExecutionThread postExecutionThread,
                                   SessionHandler sessionHandler,
                                   ValidateOtpUseCase validateOtpUseCase,
                                   MakeLoginUseCase makeLoginUseCase) {
        super(threadExecutor, postExecutionThread);
        this.sessionHandler = sessionHandler;
        this.validateOtpUseCase = validateOtpUseCase;
        this.makeLoginUseCase = makeLoginUseCase;
    }

    @Override
    public Observable<ValidateOtpLoginDomain> createObservable(RequestParams requestParams) {
        ValidateOtpLoginDomain domain = new ValidateOtpLoginDomain();
        return validateOTP(requestParams, domain)
                .flatMap(makeLogin(requestParams, domain));
    }

    private Observable<ValidateOtpLoginDomain> validateOTP(RequestParams requestParams,
                                                           final ValidateOtpLoginDomain domain) {
        return validateOtpUseCase.createObservable(ValidateOtpUseCase.getParam(
                requestParams.getString(ValidateOtpUseCase.PARAM_USER, ""),
                requestParams.getInt(ValidateOtpUseCase.PARAM_OTP_TYPE, -1),
                requestParams.getString(ValidateOtpUseCase.PARAM_CODE, "")
        )).flatMap(new Func1<ValidateOtpDomain, Observable<ValidateOtpLoginDomain>>() {
            @Override
            public Observable<ValidateOtpLoginDomain> call(ValidateOtpDomain validateOTPDomain) {
                if (validateOTPDomain.isSuccess())
                    domain.setValidateOtpDomain(validateOTPDomain);
                return Observable.just(domain);
            }
        });
    }

    private Func1<ValidateOtpLoginDomain, Observable<ValidateOtpLoginDomain>> makeLogin(final RequestParams requestParams,
                                                                                        final ValidateOtpLoginDomain domain) {
        return new Func1<ValidateOtpLoginDomain, Observable<ValidateOtpLoginDomain>>() {
            @Override
            public Observable<ValidateOtpLoginDomain> call(ValidateOtpLoginDomain validateOTPLoginDomain) {
                return makeLoginUseCase.createObservable(MakeLoginUseCase.getParam(
                        requestParams.getString(MakeLoginUseCase.PARAM_USER_ID, "")
                ))
                        .flatMap(new Func1<MakeLoginDomain, Observable<ValidateOtpLoginDomain>>() {
                            @Override
                            public Observable<ValidateOtpLoginDomain> call(MakeLoginDomain makeLoginDomain) {
                                domain.setMakeLoginDomain(makeLoginDomain);
                                return Observable.just(domain);
                            }
                        });
            }
        };
    }

    public static RequestParams getParam(int otpType, String otp, String tempUserId) {
        RequestParams params = RequestParams.create();
        params.putAll(ValidateOtpUseCase.getParam(tempUserId, otpType, otp).getParameters());
        params.putAll(MakeLoginUseCase.getParam(tempUserId).getParameters());
        return params;
    }
}
