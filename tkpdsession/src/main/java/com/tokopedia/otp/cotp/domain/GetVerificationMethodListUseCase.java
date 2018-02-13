package com.tokopedia.otp.cotp.domain;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.otp.cotp.data.VerificationMethodSource;
import com.tokopedia.otp.cotp.view.viewmodel.ListVerificationMethod;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author by nisie on 1/18/18.
 */

public class GetVerificationMethodListUseCase extends UseCase<ListVerificationMethod> {
    public static final String PARAM_MSISDN = "msisdn";
    private static final String PARAM_OTP_TYPE = "otp_type";
    private static final String PARAM_USER_ID = "user_id";

    private final VerificationMethodSource source;

    @Inject
    public GetVerificationMethodListUseCase(ThreadExecutor threadExecutor,
                                            PostExecutionThread postExecutionThread,
                                            VerificationMethodSource source) {
        super(threadExecutor, postExecutionThread);
        this.source = source;
    }

    @Override
    public Observable<ListVerificationMethod> createObservable(RequestParams requestParams) {
        return source.getMethodList(requestParams);
    }

    public static RequestParams getParam(String phoneNumber, int otpType, String userId) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(PARAM_MSISDN, phoneNumber);
        requestParams.putInt(PARAM_OTP_TYPE, otpType);
        requestParams.putString(PARAM_USER_ID, userId);
        return requestParams;
    }
}
