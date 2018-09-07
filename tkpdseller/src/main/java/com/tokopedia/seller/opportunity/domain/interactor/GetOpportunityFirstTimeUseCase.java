package com.tokopedia.seller.opportunity.domain.interactor;

import android.support.annotation.Nullable;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.seller.opportunity.data.OpportunityFilterModel;
import com.tokopedia.seller.opportunity.data.OpportunityModel;
import com.tokopedia.seller.opportunity.domain.model.OpportunityFirstTimeModel;
import com.tokopedia.seller.opportunity.viewmodel.opportunitylist.FilterPass;

import java.util.ArrayList;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;
import rx.functions.Func2;

/**
 * @author by nisie on 6/2/17.
 */

public class GetOpportunityFirstTimeUseCase extends UseCase<OpportunityFirstTimeModel> {

    public static final String PAGE = "page";
    public static final String QUERY = "search";
    public static final String LIST_FILTER = "list_filter";
    public static final String SHOP_ID = "shop_id";
    public static final String ORDER_BY = "order_by";


    private final GetOpportunityUseCase getOpportunityUseCase;
    private final GetOpportunityFilterUseCase getOpportunityFilterUseCase;

    @Inject
    public GetOpportunityFirstTimeUseCase(ThreadExecutor threadExecutor,
                                          PostExecutionThread postExecutionThread,
                                          GetOpportunityUseCase getOpportunityUseCase,
                                          GetOpportunityFilterUseCase getOpportunityFilterUseCase) {
        super(threadExecutor, postExecutionThread);
        this.getOpportunityUseCase = getOpportunityUseCase;
        this.getOpportunityFilterUseCase = getOpportunityFilterUseCase;
    }

    @Override
    public Observable<OpportunityFirstTimeModel> createObservable(RequestParams requestParams) {
        return Observable.zip(
                getOpportunityUseCase.createObservable(getListParam(requestParams)),
                getOpportunityFilterUseCase.createObservable(getFilterParam(requestParams)),
                new Func2<OpportunityModel, OpportunityFilterModel, OpportunityFirstTimeModel>() {
                    @Override
                    public OpportunityFirstTimeModel call(OpportunityModel opportunityModel, OpportunityFilterModel opportunityFilterModel) {
                        return new OpportunityFirstTimeModel(opportunityModel, opportunityFilterModel);
                    }
                });
    }

    private RequestParams getListParam(RequestParams requestParams) {
        return GetOpportunityUseCase.getRequestParam(
                requestParams.getInt(PAGE, 1),
                requestParams.getString(QUERY, ""),
                (ArrayList<FilterPass>) requestParams.getObject(LIST_FILTER)
        );
    }

    private RequestParams getFilterParam(RequestParams requestParams) {
        return GetOpportunityFilterUseCase.getRequestParam(requestParams.getString(SHOP_ID, ""));
    }

    public static RequestParams getRequestParam(int page,
                                                @Nullable String query,
                                                @Nullable ArrayList<FilterPass> listFilter,
                                                String shopId) {
        RequestParams params = RequestParams.create();
        params.putInt(PAGE, page);
        if (query != null)
            params.putString(QUERY, query);
        if (listFilter != null)
            params.putObject(LIST_FILTER, listFilter);
        params.putString(SHOP_ID, shopId);
        return params;
    }
}
