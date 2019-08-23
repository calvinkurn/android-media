package com.tokopedia.discovery.newdiscovery.domain.usecase;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.discovery.common.data.DynamicFilterModel;
import com.tokopedia.discovery.newdiscovery.data.repository.AttributeRepository;

import rx.Observable;

public class GetDynamicFilterV4UseCase extends UseCase<DynamicFilterModel> {

    private final AttributeRepository repository;

    public GetDynamicFilterV4UseCase(ThreadExecutor threadExecutor,
                                   PostExecutionThread postExecutionThread,
                                   AttributeRepository repository) {
        super(threadExecutor, postExecutionThread);
        this.repository = repository;
    }

    @Override
    public Observable<DynamicFilterModel> createObservable(RequestParams requestParams) {
        return repository.getDynamicFilterV4(requestParams.getParameters());
    }
}