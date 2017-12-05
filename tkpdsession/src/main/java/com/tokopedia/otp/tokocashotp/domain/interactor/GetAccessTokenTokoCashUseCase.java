package com.tokopedia.otp.tokocashotp.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.otp.tokocashotp.data.source.TokoCashTokenSource;
import com.tokopedia.otp.tokocashotp.domain.model.AccessTokenTokoCashDomain;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author by nisie on 12/5/17.
 */

public class GetAccessTokenTokoCashUseCase extends UseCase<AccessTokenTokoCashDomain> {

    private final TokoCashTokenSource tokoCashTokenSource;

    @Inject
    public GetAccessTokenTokoCashUseCase(ThreadExecutor threadExecutor,
                                         PostExecutionThread postExecutionThread,
                                         TokoCashTokenSource tokoCashTokenSource) {
        super(threadExecutor, postExecutionThread);
        this.tokoCashTokenSource = tokoCashTokenSource;
    }

    @Override
    public Observable<AccessTokenTokoCashDomain> createObservable(RequestParams requestParams) {
        return tokoCashTokenSource.getAccessToken(requestParams.getParameters());
    }

}
