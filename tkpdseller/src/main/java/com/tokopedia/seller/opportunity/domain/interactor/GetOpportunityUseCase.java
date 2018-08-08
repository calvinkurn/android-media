package com.tokopedia.seller.opportunity.domain.interactor;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.seller.opportunity.analytics.OpportunityTrackingEventLabel;
import com.tokopedia.seller.opportunity.data.OpportunityModel;
import com.tokopedia.seller.opportunity.domain.repository.ReplacementRepository;
import com.tokopedia.seller.opportunity.viewmodel.opportunitylist.FilterPass;

import java.util.ArrayList;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by nisie on 3/3/17.
 */

public class GetOpportunityUseCase extends UseCase<OpportunityModel> {

    public static final String PER_PAGE = "per_page";
    public static final String PAGE = "page";
    public static final String QUERY = "search";

    public static final String DEFAULT_PER_PAGE = "10";

    private final ReplacementRepository repository;

    @Inject
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


    public static RequestParams getRequestParam(int page,
                                                @Nullable String query,
                                                @Nullable ArrayList<FilterPass> listFilter
    ) {
        RequestParams param = RequestParams.create();
        param.putString(GetOpportunityUseCase.PAGE, String.valueOf(page));
        param.putString(GetOpportunityUseCase.PER_PAGE, GetOpportunityUseCase.DEFAULT_PER_PAGE);
        if (!TextUtils.isEmpty(query)) {
            param.putString(GetOpportunityUseCase.QUERY, query);
        }
        if (listFilter != null && listFilter.size() > 0) {
            for (FilterPass filterPass : listFilter) {
                if (param.getString(filterPass.getKey(), "").equals(""))
                    param.putString(filterPass.getKey(), filterPass.getValue());
                else {
                    param.putString(filterPass.getKey(), param.getString(filterPass.getKey(), "")
                            + "," + filterPass.getValue());
                }
            }
        }
        return param;
    }
}
