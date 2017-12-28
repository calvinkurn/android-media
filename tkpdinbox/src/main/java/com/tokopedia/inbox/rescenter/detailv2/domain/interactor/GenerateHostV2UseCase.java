package com.tokopedia.inbox.rescenter.detailv2.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.inbox.rescenter.detailv2.domain.ResCenterRepository;
import com.tokopedia.inbox.rescenter.discussion.domain.model.generatehost.GenerateHostModel;

import rx.Observable;

/**
 * Created by hangnadi on 7/3/17.
 */

public class GenerateHostV2UseCase extends UseCase<GenerateHostModel> {

    private final ResCenterRepository repository;

    public GenerateHostV2UseCase(ThreadExecutor jobExecutor,
                               PostExecutionThread uiThread,
                               ResCenterRepository repository) {
        super(jobExecutor, uiThread);
        this.repository = repository;
    }

    @Override
    public Observable<GenerateHostModel> createObservable(RequestParams requestParams) {
        return repository.generateToken(requestParams.getParameters());
    }
}
