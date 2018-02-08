package com.tokopedia.tokocash.qrpayment.domain;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.tokocash.qrpayment.data.repository.QrPaymentRepository;
import com.tokopedia.tokocash.qrpayment.presentation.model.BalanceTokoCash;

import rx.Observable;

/**
 * Created by nabillasabbaha on 1/4/18.
 */

public class GetBalanceTokoCashUseCase extends UseCase<BalanceTokoCash> {

    private QrPaymentRepository repository;

    public GetBalanceTokoCashUseCase(ThreadExecutor threadExecutor,
                                     PostExecutionThread postExecutionThread,
                                     QrPaymentRepository repository) {
        super(threadExecutor, postExecutionThread);
        this.repository = repository;
    }

    @Override
    public Observable<BalanceTokoCash> createObservable(RequestParams requestParams) {
        return repository.getBalanceTokoCash(requestParams.getParameters());
    }
}
