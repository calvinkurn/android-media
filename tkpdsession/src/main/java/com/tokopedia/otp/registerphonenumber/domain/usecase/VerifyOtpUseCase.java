package com.tokopedia.otp.registerphonenumber.domain.usecase;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.otp.tokocashotp.data.source.TokoCashOtpSource;
import com.tokopedia.otp.tokocashotp.view.viewmodel.VerifyOtpTokoCashViewModel;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author by yfsx on 5/3/17.
 */

public class VerifyOtpUseCase extends UseCase<VerifyOtpTokoCashViewModel> {

    private static final String PARAM_PHONE_NUMBER = "msisdn";
    private static final String PARAM_OTP_CODE = "otp";

    private final TokoCashOtpSource tokoCashLoginSource;

    @Inject
    public VerifyOtpUseCase(ThreadExecutor threadExecutor,
                            PostExecutionThread postExecutionThread,
                            TokoCashOtpSource tokoCashLoginSource) {
        super(threadExecutor, postExecutionThread);
        this.tokoCashLoginSource = tokoCashLoginSource;
    }

    public static RequestParams getParam(String phoneNumber, String otpCode) {
        RequestParams params = RequestParams.create();
        params.putString(PARAM_PHONE_NUMBER, phoneNumber);
        params.putString(PARAM_OTP_CODE, otpCode);
        return params;
    }

    @Override
    public Observable<VerifyOtpTokoCashViewModel> createObservable(RequestParams requestParams) {
        return tokoCashLoginSource.verifyOtpTokoCash(requestParams.getParameters());
    }
}
