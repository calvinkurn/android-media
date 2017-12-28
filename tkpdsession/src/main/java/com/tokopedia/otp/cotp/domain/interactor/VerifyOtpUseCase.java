package com.tokopedia.otp.cotp.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.otp.cotp.data.source.CentralizedOtpSource;
import com.tokopedia.otp.cotp.view.viewmodel.VerifyOtpViewModel;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author by nisie on 12/4/17.
 */

public class VerifyOtpUseCase extends UseCase<VerifyOtpViewModel> {

    private static final String PARAM_PHONE_NUMBER = "msisdn";
    private static final String PARAM_OTP_CODE = "otp";

    private final CentralizedOtpSource tokoCashLoginSource;

    @Inject
    public VerifyOtpUseCase(ThreadExecutor threadExecutor,
                            PostExecutionThread postExecutionThread,
                            CentralizedOtpSource tokoCashLoginSource) {
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
    public Observable<VerifyOtpViewModel> createObservable(RequestParams requestParams) {
        return tokoCashLoginSource.verifyOtpTokoCash(requestParams.getParameters());
    }
}
