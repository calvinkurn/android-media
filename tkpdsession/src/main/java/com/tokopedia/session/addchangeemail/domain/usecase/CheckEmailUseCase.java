package com.tokopedia.session.addchangeemail.domain.usecase;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.session.addchangeemail.data.source.CheckEmailSource;
import com.tokopedia.session.addchangeemail.view.viewmodel.CheckEmailViewModel;

import rx.Observable;

/**
 * @author by yfsx on 09/03/18.
 */

public class CheckEmailUseCase extends UseCase<CheckEmailViewModel> {
    private final static String PARAM_EMAIL = "email";

    private final CheckEmailSource checkEmailSource;

    public CheckEmailUseCase(ThreadExecutor threadExecutor,
                             PostExecutionThread postExecutionThread,
                             CheckEmailSource checkEmailSource) {
        super(threadExecutor, postExecutionThread);
        this.checkEmailSource = checkEmailSource;
    }

    @Override
    public Observable<CheckEmailViewModel> createObservable(RequestParams requestParams) {
        return checkEmailSource.checkEmail(requestParams);
    }

    public static RequestParams getParams(String email) {
        RequestParams params = RequestParams.create();
        params.putString(PARAM_EMAIL, email);
        return params;
    }
}
