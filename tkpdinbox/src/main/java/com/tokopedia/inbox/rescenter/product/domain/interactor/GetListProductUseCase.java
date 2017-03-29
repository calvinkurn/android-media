package com.tokopedia.inbox.rescenter.product.domain.interactor;

import com.tokopedia.core.base.data.executor.JobExecutor;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.presentation.UIThread;
import com.tokopedia.inbox.rescenter.detailv2.domain.ResCenterRepository;
import com.tokopedia.inbox.rescenter.product.domain.model.ListProductDomainData;

import rx.Observable;

/**
 * Created by hangnadi on 3/27/17.
 */

public class GetListProductUseCase extends UseCase<ListProductDomainData> {

    private final ResCenterRepository resCenterRepository;

    public GetListProductUseCase(JobExecutor jobExecutor,
                                 UIThread uiThread, ResCenterRepository resCenterRepository) {
        super(jobExecutor, uiThread);
        this.resCenterRepository = resCenterRepository;
    }

    @Override
    public Observable<ListProductDomainData> createObservable(RequestParams requestParams) {
        return resCenterRepository.getListProduct(requestParams.getParameters());
    }
}
