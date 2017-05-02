package com.tokopedia.inbox.rescenter.historyaddress.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.inbox.rescenter.detailv2.domain.ResCenterRepository;
import com.tokopedia.inbox.rescenter.historyaddress.domain.model.HistoryAddressData;

import rx.Observable;

/**
 * Created by hangnadi on 3/27/17.
 */

public class GetHistoryAddressUseCase extends UseCase<HistoryAddressData> {

    public static final String ARGS_PARAM_RESOLUTION_ID = "resolution_id";

    private final ResCenterRepository resCenterRepository;

    public GetHistoryAddressUseCase(ThreadExecutor jobExecutor,
                                    PostExecutionThread uiThread, ResCenterRepository resCenterRepository) {
        super(jobExecutor, uiThread);
        this.resCenterRepository = resCenterRepository;
    }

    @Override
    public Observable<HistoryAddressData> createObservable(RequestParams requestParams) {
        String resolutionID = requestParams.getString(ARGS_PARAM_RESOLUTION_ID, "");
        return resCenterRepository.getHistoryAddress(resolutionID, requestParams.getParameters());
    }
}
