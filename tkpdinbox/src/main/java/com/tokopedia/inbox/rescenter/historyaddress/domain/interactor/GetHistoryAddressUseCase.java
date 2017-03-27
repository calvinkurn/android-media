package com.tokopedia.inbox.rescenter.historyaddress.domain.interactor;

import com.tokopedia.core.base.data.executor.JobExecutor;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.presentation.UIThread;
import com.tokopedia.inbox.rescenter.detailv2.domain.ResCenterRepository;
import com.tokopedia.inbox.rescenter.historyaddress.domain.model.HistoryAddressData;

import rx.Observable;

/**
 * Created by hangnadi on 3/27/17.
 */

public class GetHistoryAddressUseCase extends UseCase<HistoryAddressData> {

    private final ResCenterRepository resCenterRepository;

    public GetHistoryAddressUseCase(JobExecutor jobExecutor,
                                    UIThread uiThread, ResCenterRepository resCenterRepository) {
        super(jobExecutor, uiThread);
        this.resCenterRepository = resCenterRepository;
    }

    @Override
    public Observable<HistoryAddressData> createObservable(RequestParams requestParams) {
        return resCenterRepository.getHistoryAddress(requestParams.getParameters());
    }
}
