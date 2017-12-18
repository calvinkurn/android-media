package com.tokopedia.otp.phoneverification.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.otp.phoneverification.data.VerifyPhoneNumberDomain;
import com.tokopedia.otp.phoneverification.data.source.VerifyMsisdnSource;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author by nisie on 3/7/17.
 */

public class VerifyPhoneNumberUseCase extends UseCase<VerifyPhoneNumberDomain> {

    public static final String PARAM_USER_ID = "user_id";
    public static final String PARAM_PHONE = "phone";

    private final VerifyMsisdnSource verifyMsisdnSource;

    @Inject
    public VerifyPhoneNumberUseCase(ThreadExecutor threadExecutor,
                                    PostExecutionThread postExecutionThread,
                                    VerifyMsisdnSource verifyMsisdnSource) {
        super(threadExecutor, postExecutionThread);
        this.verifyMsisdnSource = verifyMsisdnSource;
    }

    @Override
    public Observable<VerifyPhoneNumberDomain> createObservable(final RequestParams requestParams) {
        return verifyMsisdnSource.verifyPhoneNumber(requestParams.getParameters());
    }

    public static RequestParams getParam(String userId, String phone) {
        RequestParams param = RequestParams.create();
        param.putString(VerifyPhoneNumberUseCase.PARAM_USER_ID, userId);
        param.putString(VerifyPhoneNumberUseCase.PARAM_PHONE, phone);
        return param;
    }

}