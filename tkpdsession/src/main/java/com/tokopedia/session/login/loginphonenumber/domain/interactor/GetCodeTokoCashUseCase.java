package com.tokopedia.session.login.loginphonenumber.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.session.login.loginphonenumber.data.source.TokoCashCodeSource;
import com.tokopedia.session.login.loginphonenumber.domain.model.CodeTokoCashDomain;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author by nisie on 12/5/17.
 */

public class GetCodeTokoCashUseCase extends UseCase<CodeTokoCashDomain> {

    public static final String PARAM_KEY = "key";
    public static final String PARAM_EMAIL = "email";
    private final TokoCashCodeSource tokoCashCodeSource;

    @Inject
    public GetCodeTokoCashUseCase(ThreadExecutor threadExecutor,
                                  PostExecutionThread postExecutionThread,
                                  TokoCashCodeSource tokoCashCodeSource) {
        super(threadExecutor, postExecutionThread);
        this.tokoCashCodeSource = tokoCashCodeSource;
    }

    @Override
    public Observable<CodeTokoCashDomain> createObservable(RequestParams requestParams) {
        return tokoCashCodeSource.getAccessToken(requestParams.getParameters());
    }

    public static RequestParams getParam(String key, String email) {
        RequestParams params = RequestParams.create();
        params.putString(PARAM_KEY, key);
        params.putString(PARAM_EMAIL, email);
        return params;
    }
}
