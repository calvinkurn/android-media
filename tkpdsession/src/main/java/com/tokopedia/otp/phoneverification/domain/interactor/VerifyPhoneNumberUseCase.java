package com.tokopedia.otp.phoneverification.domain.interactor;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.otp.data.model.ValidateOtpModel;
import com.tokopedia.otp.domainold.ValidateOtpUseCase;
import com.tokopedia.otp.phoneverification.data.VerifyPhoneNumberModel;
import com.tokopedia.otp.phoneverification.domain.MsisdnRepository;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by nisie on 3/7/17.
 */

public class VerifyPhoneNumberUseCase extends UseCase<VerifyPhoneNumberModel> {

    public static final String PARAM_USER_ID = "user_id";
    public static final String PARAM_PHONE = "phone";

    private final MsisdnRepository msisdnRepository;
    private final ValidateOtpUseCase validateOtpUseCase;

    public VerifyPhoneNumberUseCase(ThreadExecutor threadExecutor,
                                    PostExecutionThread postExecutionThread,
                                    MsisdnRepository msisdnRepository,
                                    ValidateOtpUseCase validateOtpUseCase) {
        super(threadExecutor, postExecutionThread);
        this.msisdnRepository = msisdnRepository;
        this.validateOtpUseCase = validateOtpUseCase;
    }

    @Override
    public Observable<VerifyPhoneNumberModel> createObservable(final RequestParams requestParams) {
        return doValidateOtp(getValidateOtpParam(requestParams))
                .flatMap(new Func1<ValidateOtpModel, Observable<VerifyPhoneNumberModel>>() {
                    @Override
                    public Observable<VerifyPhoneNumberModel> call(ValidateOtpModel validateOtpModel) {
                        return doVerifyPhoneNumber(getVerifyPhoneNumberParam(requestParams));
                    }
                });
    }

    private RequestParams getVerifyPhoneNumberParam(RequestParams requestParams) {
        RequestParams param = RequestParams.create();
        param.putString(VerifyPhoneNumberUseCase.PARAM_USER_ID,
                requestParams.getString(VerifyPhoneNumberUseCase.PARAM_USER_ID,
                SessionHandler.getLoginID(MainApplication.getAppContext())));
        param.putString(VerifyPhoneNumberUseCase.PARAM_PHONE,
                requestParams.getString(VerifyPhoneNumberUseCase.PARAM_PHONE,
                ""));
        return param;
    }

    private RequestParams getValidateOtpParam(RequestParams requestParams) {
        RequestParams param = RequestParams.create();
        param.putString(ValidateOtpUseCase.PARAM_CODE,
                requestParams.getString(ValidateOtpUseCase.PARAM_CODE, ""));
        param.putString(ValidateOtpUseCase.PARAM_USER,
                requestParams.getString(ValidateOtpUseCase.PARAM_USER, ""));
        return param;
    }

    private Observable<VerifyPhoneNumberModel> doVerifyPhoneNumber(RequestParams requestParams) {
        return msisdnRepository.verifyMsisdn(requestParams.getParameters());
    }

    private Observable<ValidateOtpModel> doValidateOtp(RequestParams requestParams) {
        return validateOtpUseCase.createObservable(requestParams)
                .flatMap(new Func1<ValidateOtpModel, Observable<ValidateOtpModel>>() {
                    @Override
                    public Observable<ValidateOtpModel> call(ValidateOtpModel validateOtpModel) {
                        return Observable.just(validateOtpModel);
                    }
                });
    }


}