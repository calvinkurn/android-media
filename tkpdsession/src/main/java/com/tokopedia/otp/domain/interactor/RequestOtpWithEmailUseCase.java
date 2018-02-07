package com.tokopedia.otp.domain.interactor;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.otp.data.model.RequestOtpViewModel;
import com.tokopedia.otp.data.source.OtpSource;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author by nisie on 1/2/18.
 */

public class RequestOtpWithEmailUseCase extends RequestOtpUseCase {

    private static final String PARAM_USER = "user";
    private static final String PARAM_TYPE = "type";

    @Inject
    public RequestOtpWithEmailUseCase(ThreadExecutor threadExecutor,
                                      PostExecutionThread postExecutionThread,
                                      OtpSource otpSource) {
        super(threadExecutor, postExecutionThread, otpSource);
    }

    @Override
    public Observable<RequestOtpViewModel> createObservable(RequestParams requestParams) {
        return otpSource.requestOtpWithEmail(requestParams.getParameters());
    }

    public static RequestParams getParam(String email, int otpType,
                                         String tempUserId) {
        RequestParams param = RequestParams.create();
        param.putString(PARAM_USER, tempUserId);
        param.putString(PARAM_EMAIL, email);
        param.putInt(PARAM_TYPE, otpType);
        param.putAll(AuthUtil.generateParamsNetworkObject(MainApplication.getAppContext(),
                param.getParameters(), tempUserId));
        return param;
    }
}
