package com.tokopedia.session.domain.interactor;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.session.data.source.MakeLoginDataSource;
import com.tokopedia.session.data.viewmodel.login.MakeLoginDomain;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author by nisie on 5/26/17.
 */

public class MakeLoginUseCase extends UseCase<MakeLoginDomain> {

    public static final String PARAM_USER_ID = "user_id";
    private final MakeLoginDataSource makeLoginDataSource;

    @Inject
    public MakeLoginUseCase(ThreadExecutor threadExecutor,
                            PostExecutionThread postExecutionThread,
                            MakeLoginDataSource makeLoginDataSource) {
        super(threadExecutor, postExecutionThread);
        this.makeLoginDataSource = makeLoginDataSource;
    }

    @Override
    public Observable<MakeLoginDomain> createObservable(RequestParams requestParams) {
        return makeLoginDataSource.makeLogin(requestParams.getParameters());
    }

    public static RequestParams getParam(String userId) {
        RequestParams params = RequestParams.create();
        params.putString(PARAM_USER_ID, userId);
        params.putAll(AuthUtil.generateParamsNetworkObject(MainApplication.getAppContext(),
                params.getParameters(),
                userId));
        return params;
    }
}
