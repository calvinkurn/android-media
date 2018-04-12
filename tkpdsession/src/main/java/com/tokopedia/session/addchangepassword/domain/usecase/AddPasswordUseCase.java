package com.tokopedia.session.addchangepassword.domain.usecase;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.session.addchangepassword.data.source.AddPasswordSource;
import com.tokopedia.session.addchangepassword.view.viewmodel.AddPasswordViewModel;

import rx.Observable;

/**
 * @author by yfsx on 09/03/18.
 */

public class AddPasswordUseCase extends UseCase<AddPasswordViewModel> {
    private final static String PARAM_USER_ID = "user_id";
    private final static String PARAM_PASSWORD = "password";

    private final AddPasswordSource addPasswordSource;

    public AddPasswordUseCase(ThreadExecutor threadExecutor,
                              PostExecutionThread postExecutionThread,
                              AddPasswordSource addPasswordSource) {
        super(threadExecutor, postExecutionThread);
        this.addPasswordSource = addPasswordSource;
    }

    @Override
    public Observable<AddPasswordViewModel> createObservable(RequestParams requestParams) {
        return addPasswordSource.addPassword(requestParams);
    }

    public static RequestParams getParams(String userId, String password) {
        RequestParams params = RequestParams.create();
        params.putString(PARAM_USER_ID, userId);
        params.putString(PARAM_PASSWORD, password);
        return params;
    }
}
