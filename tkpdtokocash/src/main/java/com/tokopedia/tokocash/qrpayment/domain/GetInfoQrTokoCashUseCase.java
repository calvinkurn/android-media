package com.tokopedia.tokocash.qrpayment.domain;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.tokocash.qrpayment.presentation.model.InfoQrTokoCash;

import rx.Observable;

/**
 * Created by nabillasabbaha on 1/2/18.
 */

public class GetInfoQrTokoCashUseCase extends UseCase<InfoQrTokoCash> {

    public static final String IDENTIFIER = "identifier";

    private IQrPaymentRepository repository;

    public GetInfoQrTokoCashUseCase(ThreadExecutor threadExecutor,
                                    PostExecutionThread postExecutionThread,
                                    IQrPaymentRepository repository) {
        super(threadExecutor, postExecutionThread);
        this.repository = repository;
    }

    @Override
    public Observable<InfoQrTokoCash> createObservable(RequestParams requestParams) {
        return repository.getInfoQrTokoCash(requestParams.getParameters());
    }
}
