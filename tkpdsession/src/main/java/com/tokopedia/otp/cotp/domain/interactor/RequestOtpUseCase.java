package com.tokopedia.otp.cotp.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.otp.cotp.data.source.CentralizedOtpSource;
import com.tokopedia.otp.cotp.view.viewmodel.RequestOtpViewModel;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author by nisie on 12/4/17.
 */

public class RequestOtpUseCase extends UseCase<RequestOtpViewModel> {

    private static final String PARAM_PHONE_NUMBER = "msisdn";
    private static final String PARAM_METHOD = "accept";
    public static final String TYPE_SMS = "sms";
    public static final String TYPE_PHONE = "call";

    private CentralizedOtpSource tokoCashLoginSource;

    @Inject
    public RequestOtpUseCase(ThreadExecutor threadExecutor,
                             PostExecutionThread postExecutionThread,
                             CentralizedOtpSource tokoCashLoginSource) {
        super(threadExecutor, postExecutionThread);
        this.tokoCashLoginSource = tokoCashLoginSource;
    }

    @Override
    public Observable<RequestOtpViewModel> createObservable(RequestParams requestParams) {
        return tokoCashLoginSource.requestLoginOtp(requestParams.getParameters());
    }

    public static RequestParams getParam(String phoneNumber, String type) {
        RequestParams params = RequestParams.create();
        params.putString(PARAM_PHONE_NUMBER, phoneNumber);
        params.putString(PARAM_METHOD, type);
        return params;
    }
}
