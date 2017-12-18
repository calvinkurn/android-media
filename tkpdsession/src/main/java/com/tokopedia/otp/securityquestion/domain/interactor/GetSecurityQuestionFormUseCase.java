package com.tokopedia.otp.securityquestion.domain.interactor;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.otp.securityquestion.data.model.securityquestion.QuestionViewModel;
import com.tokopedia.otp.securityquestion.data.source.SecurityQuestionDataSource;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author by nisie on 10/19/17.
 */

public class GetSecurityQuestionFormUseCase extends UseCase<QuestionViewModel> {

    private static final String USER_CHECK_SECURITY_ONE = "user_check_security_1";
    private static final String USER_CHECK_SECURITY_TWO = "user_check_security_2";
    private static final String USER_ID = "user_id";

    private final SessionHandler sessionHandler;
    private final SecurityQuestionDataSource securityQuestionDataSource;

    @Inject
    public GetSecurityQuestionFormUseCase(ThreadExecutor threadExecutor,
                                          PostExecutionThread postExecutionThread,
                                          SecurityQuestionDataSource securityQuestionDataSource,
                                          SessionHandler sessionHandler) {
        super(threadExecutor, postExecutionThread);
        this.securityQuestionDataSource = securityQuestionDataSource;
        this.sessionHandler = sessionHandler;
    }

    @Override
    public Observable<QuestionViewModel> createObservable(RequestParams requestParams) {
        return securityQuestionDataSource.getSecurityQuestion(requestParams);
    }

    public RequestParams getParam(int userCheckSecurity1, int userCheckSecurity2) {
        RequestParams params = RequestParams.create();
        params.putInt(USER_CHECK_SECURITY_ONE, userCheckSecurity1);
        params.putInt(USER_CHECK_SECURITY_TWO, userCheckSecurity2);
        params.putString(USER_ID, sessionHandler.getTempLoginSession(MainApplication.getAppContext()));
        params.putAll(AuthUtil.generateParamsNetworkObject(MainApplication.getAppContext(),
                params.getParameters(),
                sessionHandler.getTempLoginSession(MainApplication.getAppContext())));
        return params;
    }
}
