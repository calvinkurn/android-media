package com.tokopedia.session.addchangeemail.domain.usecase;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.session.addchangeemail.data.source.AddEmailSource;
import com.tokopedia.session.addchangeemail.view.viewmodel.CheckEmailViewModel;

import rx.Observable;

/**
 * @author by yfsx on 09/03/18.
 */

public class CheckEmailUseCase extends UseCase<CheckEmailViewModel> {
    private final static String PARAM_EMAIL = "email";

    private final AddEmailSource addEmailSource;

    public CheckEmailUseCase(ThreadExecutor threadExecutor,
                             PostExecutionThread postExecutionThread,
                             AddEmailSource addEmailSource) {
        super(threadExecutor, postExecutionThread);
        this.addEmailSource = addEmailSource;
    }

    @Override
    public Observable<CheckEmailViewModel> createObservable(RequestParams requestParams) {
        return addEmailSource.checkEmail(requestParams);
    }

    public static RequestParams getParams(String email) {
        RequestParams params = RequestParams.create();
        params.putString(PARAM_EMAIL, email);
        return params;
    }
}
