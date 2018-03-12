package com.tokopedia.tokocash.pendingcashback.domain;

import com.tokopedia.tokocash.pendingcashback.data.PendingCashbackRepository;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import rx.Observable;

/**
 * Created by nabillasabbaha on 2/7/18.
 */

public class GetPendingCasbackUseCase extends UseCase<PendingCashback> {

    private PendingCashbackRepository repository;

    public GetPendingCasbackUseCase(PendingCashbackRepository repository) {
        this.repository = repository;
    }

    @Override
    public Observable<PendingCashback> createObservable(RequestParams requestParams) {
        return repository.getPendingCashback(requestParams.getParamsAllValueInString());
    }
}
