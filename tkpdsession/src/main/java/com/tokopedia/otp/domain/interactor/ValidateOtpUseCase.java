package com.tokopedia.otp.domain.interactor;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.otp.data.model.ValidateOtpDomain;
import com.tokopedia.otp.data.source.OtpSource;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author by nisie on 10/21/17.
 */

public class ValidateOtpUseCase extends UseCase<ValidateOtpDomain> {
    public static final String PARAM_OTP_TYPE = "otp_type";
    public static final String PARAM_USER = "user";
    public static final String PARAM_CODE = "code";

    public static final int OTP_TYPE_SECURITY_QUESTION = 13;
    public static final int OTP_TYPE_PHONE_NUMBER_VERIFICATION = 11;
    public static final int OTP_TYPE_CHANGE_PHONE_NUMBER = 20;


    private final OtpSource otpSource;

    @Inject
    public ValidateOtpUseCase(ThreadExecutor threadExecutor,
                              PostExecutionThread postExecutionThread,
                              OtpSource otpSource) {
        super(threadExecutor, postExecutionThread);
        this.otpSource = otpSource;
    }

    @Override
    public Observable<ValidateOtpDomain> createObservable(RequestParams requestParams) {
        return otpSource.validateOtp(requestParams.getParameters());
    }

    public static RequestParams getParam(String userId, int otpType, String otp) {
        RequestParams param = RequestParams.create();
        param.putString(PARAM_USER, userId);
        param.putInt(PARAM_OTP_TYPE, otpType);
        param.putString(PARAM_CODE, otp);
        return param;
    }
}
