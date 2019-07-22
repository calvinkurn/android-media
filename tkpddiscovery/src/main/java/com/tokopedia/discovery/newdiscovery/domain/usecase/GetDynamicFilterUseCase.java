package com.tokopedia.discovery.newdiscovery.domain.usecase;

import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.discovery.common.data.DynamicFilterModel;
import com.tokopedia.discovery.newdiscovery.data.repository.AttributeRepository;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import rx.Observable;

/**
 * Created by hangnadi on 10/16/17.
 */

public class GetDynamicFilterUseCase extends UseCase<DynamicFilterModel> {

    private final AttributeRepository repository;

    public GetDynamicFilterUseCase(ThreadExecutor threadExecutor,
                                   PostExecutionThread postExecutionThread,
                                   AttributeRepository repository) {
        this.repository = repository;
    }

    @Override
    public Observable<DynamicFilterModel> createObservable(RequestParams requestParams) {
        TKPDMapParam<String,Object> tkpdMapParam = new TKPDMapParam<>();
        tkpdMapParam.putAll(requestParams.getParameters());
        return repository.getDynamicFilter(tkpdMapParam);
    }
}
