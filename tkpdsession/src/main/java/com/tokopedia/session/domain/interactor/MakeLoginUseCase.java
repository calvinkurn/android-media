package com.tokopedia.session.domain.interactor;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.session.data.repository.SessionRepository;
import com.tokopedia.session.data.viewmodel.login.MakeLoginDomain;

import rx.Observable;

/**
 * @author by nisie on 5/26/17.
 */

public class MakeLoginUseCase extends UseCase<MakeLoginDomain> {

    public static final String PARAM_USER_ID = "user_id";
    private final SessionRepository sessionRepository;

    public MakeLoginUseCase(ThreadExecutor threadExecutor,
                            PostExecutionThread postExecutionThread,
                            SessionRepository sessionRepository) {
        super(threadExecutor, postExecutionThread);
        this.sessionRepository = sessionRepository;
    }

    @Override
    public Observable<MakeLoginDomain> createObservable(RequestParams requestParams) {
        return sessionRepository.makeLogin(requestParams.getParameters());
    }

    public static RequestParams getParam(String userId) {
        RequestParams params = RequestParams.create();
        params.putAll(AuthUtil.generateParamsNetwork2(MainApplication.getAppContext(),
                RequestParams.EMPTY.getParameters()));
        params.putString(PARAM_USER_ID, userId);
        return params;
    }
}
