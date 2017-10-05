package com.tokopedia.seller.opportunity.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.seller.opportunity.data.OpportunityFilterModel;
import com.tokopedia.seller.opportunity.domain.repository.ReplacementRepository;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by nisie on 3/6/17.
 */

public class GetOpportunityFilterUseCase extends UseCase<OpportunityFilterModel> {

    public static final String SHOP_ID = "shop_id";
    public static final String FILTER_CACHE = "OPPORTUNITY_FILTER_CACHE";

    private final ReplacementRepository repository;

    public GetOpportunityFilterUseCase(ThreadExecutor threadExecutor,
                                       PostExecutionThread postExecutionThread,
                                       ReplacementRepository repository) {
        super(threadExecutor, postExecutionThread);
        this.repository = repository;
    }

    @Override
    public Observable<OpportunityFilterModel> createObservable(final RequestParams requestParams) {
        return repository.getOpportunityCategoryFromLocal()
                .onErrorResumeNext(new Func1<Throwable, Observable<OpportunityFilterModel>>() {
                    @Override
                    public Observable<OpportunityFilterModel> call(Throwable throwable) {
                        return repository.getOpportunityCategoryFromNetwork(requestParams.getParameters());
                    }
                });
    }

    public static RequestParams getRequestParam(String shopID) {
        RequestParams params = RequestParams.create();
        params.putString(GetOpportunityFilterUseCase.SHOP_ID, shopID);
        return params;
    }
}
