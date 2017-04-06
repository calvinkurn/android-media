package com.tokopedia.seller.opportunity.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.seller.opportunity.data.OpportunityModel;
import com.tokopedia.seller.opportunity.domain.ReplacementRepository;

import rx.Observable;

/**
 * Created by nisie on 3/3/17.
 */

public class GetOpportunityUseCase extends UseCase<OpportunityModel> {

    public static final String PER_PAGE = "per_page";
    public static final String PAGE = "page";
    public static final String QUERY = "query";
    public static final String CAT_1 = "cat_1";
    public static final String CAT_2 = "cat_2";
    public static final String CAT_3 = "cat_3";
    public static final String SHIP_TYPE = "ship_type";
    public static final String ORDER_BY = "order_by";

    public static final String DEFAULT_PER_PAGE = "10";

    private final ReplacementRepository repository;

    public GetOpportunityUseCase(ThreadExecutor threadExecutor,
                                 PostExecutionThread postExecutionThread,
                                 ReplacementRepository repository) {
        super(threadExecutor, postExecutionThread);
        this.repository = repository;
    }

    @Override
    public Observable<OpportunityModel> createObservable(RequestParams requestParams) {
        return repository.getOpportunityListFromNetwork(requestParams.getParameters());
    }


}
