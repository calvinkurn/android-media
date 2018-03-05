package com.tokopedia.otp.registerphonenumber.domain.usecase;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.otp.tokocashotp.data.source.TokoCashOtpSource;
import com.tokopedia.otp.tokocashotp.view.viewmodel.RequestOtpTokoCashViewModel;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author by yfsx on 5/3/17.
 */

public class RequestOtpUseCase extends UseCase<RequestOtpTokoCashViewModel> {

    private static final String PARAM_PHONE_NUMBER = "msisdn";
    private static final String PARAM_METHOD = "accept";
    public static final String TYPE_SMS = "sms";
    public static final String TYPE_PHONE = "call";

    private TokoCashOtpSource tokoCashLoginSource;

    @Inject
    public RequestOtpUseCase(ThreadExecutor threadExecutor,
                             PostExecutionThread postExecutionThread,
                             TokoCashOtpSource tokoCashLoginSource) {
        super(threadExecutor, postExecutionThread);
        this.tokoCashLoginSource = tokoCashLoginSource;
    }

    @Override
    public Observable<RequestOtpTokoCashViewModel> createObservable(RequestParams requestParams) {
        return tokoCashLoginSource.requestLoginOtp(requestParams.getParameters());
    }

    public static RequestParams getParam(String phoneNumber, String type) {
        RequestParams params = RequestParams.create();
        params.putString(PARAM_PHONE_NUMBER, phoneNumber);
        params.putString(PARAM_METHOD, type);
        return params;
    }
}
