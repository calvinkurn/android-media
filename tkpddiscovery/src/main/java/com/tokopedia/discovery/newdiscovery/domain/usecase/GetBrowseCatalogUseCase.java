package com.tokopedia.discovery.newdiscovery.domain.usecase;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.discovery.newdiscovery.data.repository.CatalogRepository;
import com.tokopedia.discovery.newdiscovery.domain.model.CatalogDomainModel;

import rx.Observable;

/**
 * Created by hangnadi on 10/12/17.
 */

public class GetBrowseCatalogUseCase extends UseCase<CatalogDomainModel> {

    private final CatalogRepository catalogRepository;

    public GetBrowseCatalogUseCase(ThreadExecutor threadExecutor,
                                   PostExecutionThread postExecutionThread,
                                   CatalogRepository catalogRepository) {
        super(threadExecutor, postExecutionThread);
        this.catalogRepository = catalogRepository;
    }

    @Override
    public Observable<CatalogDomainModel> createObservable(RequestParams requestParams) {
        return catalogRepository.getBrowseCatalog(requestParams.getParameters());
    }
}
