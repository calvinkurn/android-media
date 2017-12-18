package com.tokopedia.discovery.newdiscovery.hotlist.domain.usecase;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.discovery.newdiscovery.data.repository.AttributeRepository;
import com.tokopedia.discovery.newdiscovery.hotlist.domain.model.HotlistAttributeModel;

import rx.Observable;

/**
 * Created by hangnadi on 10/6/17.
 */

public class GetHotlistAttributeUseCase extends UseCase<HotlistAttributeModel> {


    private final AttributeRepository attributeRepository;

    public GetHotlistAttributeUseCase(
            ThreadExecutor threadExecutor,
            PostExecutionThread postExecutionThread,
            AttributeRepository attributeRepository) {
        super(threadExecutor, postExecutionThread);
        this.attributeRepository = attributeRepository;
    }

    @Override
    public Observable<HotlistAttributeModel> createObservable(RequestParams requestParams) {
        return attributeRepository.getHotlistAttribute(requestParams.getParameters());
    }
}
