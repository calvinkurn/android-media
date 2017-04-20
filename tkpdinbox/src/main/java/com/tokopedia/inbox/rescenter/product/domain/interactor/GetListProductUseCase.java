package com.tokopedia.inbox.rescenter.product.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.inbox.rescenter.detailv2.domain.ResCenterRepository;
import com.tokopedia.inbox.rescenter.product.domain.model.ListProductDomainData;

import rx.Observable;

/**
 * Created by hangnadi on 3/27/17.
 */

public class GetListProductUseCase extends UseCase<ListProductDomainData> {

    public static final String PARAM_RESOLUTION_ID = "resolution_id";

    private final ResCenterRepository resCenterRepository;

    public GetListProductUseCase(ThreadExecutor jobExecutor,
                                 PostExecutionThread uiThread,
                                 ResCenterRepository resCenterRepository) {
        super(jobExecutor, uiThread);
        this.resCenterRepository = resCenterRepository;
    }

    @Override
    public Observable<ListProductDomainData> createObservable(RequestParams requestParams) {
        String resolutionID = requestParams.getString(PARAM_RESOLUTION_ID, "");
        return resCenterRepository.getListProduct(resolutionID, requestParams.getParameters());
    }
}
