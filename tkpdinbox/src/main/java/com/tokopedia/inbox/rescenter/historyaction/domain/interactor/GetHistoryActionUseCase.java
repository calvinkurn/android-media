package com.tokopedia.inbox.rescenter.historyaction.domain.interactor;

import com.tokopedia.core.base.data.executor.JobExecutor;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.presentation.UIThread;
import com.tokopedia.inbox.rescenter.detailv2.domain.ResCenterRepository;
import com.tokopedia.inbox.rescenter.historyaction.domain.model.HistoryActionData;

import rx.Observable;

/**
 * Created by hangnadi on 3/27/17.
 */

public class GetHistoryActionUseCase extends UseCase<HistoryActionData> {

    private final ResCenterRepository resCenterRepository;

    public GetHistoryActionUseCase(JobExecutor jobExecutor,
                                   UIThread uiThread, ResCenterRepository resCenterRepository) {
        super(jobExecutor, uiThread);
        this.resCenterRepository = resCenterRepository;
    }

    @Override
    public Observable<HistoryActionData> createObservable(RequestParams requestParams) {
        return resCenterRepository.getHistoryAction(requestParams.getParameters());
    }
}
