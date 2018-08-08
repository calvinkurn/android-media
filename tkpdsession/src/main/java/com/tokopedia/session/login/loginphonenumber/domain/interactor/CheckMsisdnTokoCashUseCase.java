package com.tokopedia.session.login.loginphonenumber.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.session.login.loginphonenumber.data.source.CheckMsisdnTokoCashSource;
import com.tokopedia.session.login.loginphonenumber.view.viewmodel.CheckMsisdnTokoCashViewModel;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author by nisie on 12/6/17.
 */

public class CheckMsisdnTokoCashUseCase extends UseCase<CheckMsisdnTokoCashViewModel> {

    private static final String PARAM_MSISDN = "msisdn";
    private CheckMsisdnTokoCashSource checkMsisdnTokoCashSource;

    @Inject
    public CheckMsisdnTokoCashUseCase(ThreadExecutor threadExecutor,
                                      PostExecutionThread postExecutionThread,
                                      CheckMsisdnTokoCashSource checkMsisdnTokoCashSource) {
        super(threadExecutor, postExecutionThread);
        this.checkMsisdnTokoCashSource = checkMsisdnTokoCashSource;
    }

    @Override
    public Observable<CheckMsisdnTokoCashViewModel> createObservable(RequestParams requestParams) {
        return checkMsisdnTokoCashSource.checkMsisdn(requestParams);
    }

    public static RequestParams getParam(String phoneNumber) {
        RequestParams params = RequestParams.create();
        params.putString(PARAM_MSISDN, phoneNumber);
        return params;
    }
}
