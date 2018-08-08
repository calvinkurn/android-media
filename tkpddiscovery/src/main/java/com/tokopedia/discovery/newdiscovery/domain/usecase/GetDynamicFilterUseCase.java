package com.tokopedia.discovery.newdiscovery.domain.usecase;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.discovery.model.DynamicFilterModel;
import com.tokopedia.discovery.newdiscovery.data.repository.AttributeRepository;

import rx.Observable;

/**
 * Created by hangnadi on 10/16/17.
 */

public class GetDynamicFilterUseCase extends UseCase<DynamicFilterModel> {

    private final AttributeRepository repository;

    public GetDynamicFilterUseCase(ThreadExecutor threadExecutor,
                                   PostExecutionThread postExecutionThread,
                                   AttributeRepository repository) {
        super(threadExecutor, postExecutionThread);
        this.repository = repository;
    }

    @Override
    public Observable<DynamicFilterModel> createObservable(RequestParams requestParams) {
        return repository.getDynamicFilter(requestParams.getParameters());
    }
}
