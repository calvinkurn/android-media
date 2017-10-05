package com.tokopedia.transaction.opportunity.domain.interactor;


import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.transaction.opportunity.data.model.CancelReplacementModel;
import com.tokopedia.transaction.opportunity.domain.repository.ReplacementRepository;

import rx.Observable;

/**
 * @author by nisie on 6/2/17.
 */

public class CancelReplacementUseCase extends UseCase<CancelReplacementModel> {


    private static final String PARAM_ORDER_ID = "order_id";
    private static final String PARAM_USER_ID = "user_id";
    private static final String PARAM_USER_TOKEN = "user_token";
    private static final String PARAM_REASON_CODE = "r_code";
    private static final int DEFAULT_CANCEL_REPLACEMENT_REASON_CODE = 9;
    private final ReplacementRepository replacementRepository;

    public CancelReplacementUseCase(ThreadExecutor threadExecutor,
                                    PostExecutionThread postExecutionThread,
                                    ReplacementRepository replacementRepository) {
        super(threadExecutor, postExecutionThread);
        this.replacementRepository = replacementRepository;
    }

    @Override
    public Observable<CancelReplacementModel> createObservable(RequestParams requestParams) {
        return replacementRepository.cancelReplacement(requestParams.getParameters());
    }

    public static RequestParams getParameters(int order_id, String user_id, String user_token) {
        RequestParams params = RequestParams.create();
        params.putInt(PARAM_ORDER_ID, order_id);
        params.putInt(PARAM_REASON_CODE, DEFAULT_CANCEL_REPLACEMENT_REASON_CODE);
        params.putString(PARAM_USER_ID, user_id);
        params.putString(PARAM_USER_TOKEN, user_token);
        return params;
    }
}
