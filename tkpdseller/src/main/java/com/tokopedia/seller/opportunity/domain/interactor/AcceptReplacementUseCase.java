package com.tokopedia.seller.opportunity.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.seller.opportunity.data.AcceptReplacementModel;
import com.tokopedia.seller.opportunity.domain.repository.ReplacementRepository;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by hangnadi on 3/3/17.
 */

public class AcceptReplacementUseCase extends UseCase<AcceptReplacementModel> {

    public static final String PARAMS_ID = "r_id";

    private final ReplacementRepository replacementRepository;

    @Inject
    public AcceptReplacementUseCase(ThreadExecutor threadExecutor,
                                    PostExecutionThread postExecutionThread,
                                    ReplacementRepository replacementRepository) {
        super(threadExecutor, postExecutionThread);
        this.replacementRepository = replacementRepository;
    }

    @Override
    public Observable<AcceptReplacementModel> createObservable(RequestParams requestParams) {
        return replacementRepository.acceptReplacement(requestParams.getParameters());
    }
}
