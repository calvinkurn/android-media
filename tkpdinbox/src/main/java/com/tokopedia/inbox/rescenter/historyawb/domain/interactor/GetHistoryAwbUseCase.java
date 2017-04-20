package com.tokopedia.inbox.rescenter.historyawb.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.inbox.rescenter.detailv2.domain.ResCenterRepository;
import com.tokopedia.inbox.rescenter.historyawb.domain.model.HistoryAwbData;

import rx.Observable;

/**
 * Created by hangnadi on 3/23/17.
 */

public class GetHistoryAwbUseCase extends UseCase<HistoryAwbData> {

    public static final String PARAM_RESOLUTION_ID = "resolution_id";

    private final ResCenterRepository repository;

    public GetHistoryAwbUseCase(ThreadExecutor jobExecutor, PostExecutionThread uiThread, ResCenterRepository resCenterRepository) {
        super(jobExecutor, uiThread);
        this.repository = resCenterRepository;
    }

    @Override
    public Observable<HistoryAwbData> createObservable(RequestParams requestParams) {
        String resolutionID = requestParams.getString(PARAM_RESOLUTION_ID, "");
        return repository.getHistoryAwb(resolutionID, requestParams.getParameters());
    }
}
