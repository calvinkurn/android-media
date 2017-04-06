package com.tokopedia.seller.opportunity.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.seller.opportunity.data.OpportunityCategoryModel;
import com.tokopedia.seller.opportunity.domain.ReplacementRepository;

import rx.Observable;

/**
 * Created by nisie on 3/6/17.
 */

public class GetOpportunityFilterUseCase extends UseCase<OpportunityCategoryModel> {

    public static final String USER_ID = "user_id";
    public static final String SHOP_ID = "shop_id";
    public static final String DEVICE_ID = "device_id";
    public static final String OS_TYPE = "os_type";

    public static final String FILTER_CACHE = "OPPORTUNITY_FILTER_CACHE";

    private final ReplacementRepository repository;

    public GetOpportunityFilterUseCase(ThreadExecutor threadExecutor,
                                       PostExecutionThread postExecutionThread,
                                       ReplacementRepository repository) {
        super(threadExecutor, postExecutionThread);
        this.repository = repository;
    }

    @Override
    public Observable<OpportunityCategoryModel> createObservable(RequestParams requestParams) {
        return repository.getOpportunityCategoryFromNetwork(requestParams.getParameters());
//        return repository.getOpportunityCategoryFromLocal()
//                .switchIfEmpty(repository.getOpportunityCategoryFromNetwork(requestParams.getParameters()));
    }
}
