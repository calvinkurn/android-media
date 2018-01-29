package com.tokopedia.seller.opportunity.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.seller.opportunity.data.OpportunityNewPriceData;
import com.tokopedia.seller.opportunity.data.constant.OpportunityConstant;
import com.tokopedia.seller.opportunity.domain.repository.ReplacementRepository;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by normansyahputa on 1/11/18.
 */

public class GetOpportunityNewPriceUseCase extends UseCase<OpportunityNewPriceData>{

    private ReplacementRepository replacementRepository;

    @Inject
    public GetOpportunityNewPriceUseCase(
            ThreadExecutor threadExecutor,
            PostExecutionThread postExecutionThread,
            ReplacementRepository replacementRepository) {
        super(threadExecutor, postExecutionThread);
        this.replacementRepository = replacementRepository;
    }

    public static RequestParams createRequestParams(String replacementId){
        RequestParams params = RequestParams.create();
        params.putString(OpportunityConstant.R_CODE, replacementId);
        return params;
    }

    @Override
    public Observable<OpportunityNewPriceData> createObservable(RequestParams requestParams) {
        return replacementRepository.getOpportunityReplacementNewPrice(requestParams);
    }
}
