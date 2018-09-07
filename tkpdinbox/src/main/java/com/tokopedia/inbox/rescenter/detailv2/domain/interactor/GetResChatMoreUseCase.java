package com.tokopedia.inbox.rescenter.detailv2.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.inbox.rescenter.detailv2.domain.ResCenterRepository;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat.ConversationListDomain;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat.DetailResChatDomain;

import rx.Observable;

/**
 * Created by milhamj on 22/11/17.
 */

public class GetResChatMoreUseCase extends UseCase<ConversationListDomain> {
    public static final String PARAMS_RESO_ID = "resolution_id";
    public static final String PARAMS_LIMIT = "limit";
    public static final String PARAMS_RESO_CONV_ID = "res_conv_id";

    private ResCenterRepository resCenterRepository;

    public GetResChatMoreUseCase(ThreadExecutor threadExecutor,
                             PostExecutionThread postExecutionThread,
                             ResCenterRepository resCenterRepository) {
        super(threadExecutor, postExecutionThread);
        this.resCenterRepository = resCenterRepository;
    }

    @Override
    public Observable<ConversationListDomain> createObservable(RequestParams requestParams) {
        return resCenterRepository.getConversationMore(requestParams);
    }

    public static RequestParams getResChatUseCaseParam(String resolutionId,
                                                       int limit,
                                                       String resolutionConversationId) {
        RequestParams params = RequestParams.create();
        params.putString(PARAMS_RESO_ID, resolutionId);
        params.putInt(PARAMS_LIMIT, limit);
        params.putString(PARAMS_RESO_CONV_ID, resolutionConversationId);
        return params;
    }
}
