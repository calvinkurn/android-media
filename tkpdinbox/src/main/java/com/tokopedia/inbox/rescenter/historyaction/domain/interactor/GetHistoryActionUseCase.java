package com.tokopedia.inbox.rescenter.historyaction.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.inbox.rescenter.detailv2.domain.ResCenterRepository;
import com.tokopedia.inbox.rescenter.historyaction.domain.model.HistoryActionData;

import rx.Observable;

/**
 * Created by hangnadi on 3/27/17.
 */

public class GetHistoryActionUseCase extends UseCase<HistoryActionData> {

    public static final String PARAM_RESOLUTION_ID = "resolution_id";

    private final ResCenterRepository resCenterRepository;

    public GetHistoryActionUseCase(ThreadExecutor jobExecutor,
                                   PostExecutionThread uiThread,
                                   ResCenterRepository resCenterRepository) {
        super(jobExecutor, uiThread);
        this.resCenterRepository = resCenterRepository;
    }

    @Override
    public Observable<HistoryActionData> createObservable(RequestParams requestParams) {
        String resolutionID = requestParams.getString(PARAM_RESOLUTION_ID, "");
        return resCenterRepository.getHistoryActionV2(resolutionID, requestParams.getParameters());
    }
}
