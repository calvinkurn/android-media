package com.tokopedia.session.changename.domain.usecase;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.session.changename.data.source.ChangeNameSource;
import com.tokopedia.session.changename.view.viewmodel.ChangeNameViewModel;

import rx.Observable;

/**
 * @author by yfsx on 09/03/18.
 */

public class ChangeNameUseCase extends UseCase<ChangeNameViewModel> {
    private final static String PARAM_USER_ID = "user_id";
    private final static String PARAM_NAME = "name";

    private final ChangeNameSource changeNameSource;

    public ChangeNameUseCase(ThreadExecutor threadExecutor,
                             PostExecutionThread postExecutionThread,
                             ChangeNameSource changeNameSource) {
        super(threadExecutor, postExecutionThread);
        this.changeNameSource = changeNameSource;
    }

    @Override
    public Observable<ChangeNameViewModel> createObservable(RequestParams requestParams) {
        return changeNameSource.changeName(requestParams);
    }

    public static RequestParams getParams(String userId, String email) {
        RequestParams params = RequestParams.create();
        params.putString(PARAM_USER_ID, userId);
        params.putString(PARAM_NAME, email);
        return params;
    }
}
