package com.tokopedia.tokocash.qrpayment.domain;

import com.tokopedia.tokocash.qrpayment.data.repository.QrPaymentRepository;
import com.tokopedia.tokocash.qrpayment.presentation.model.BalanceTokoCash;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import rx.Observable;

/**
 * Created by nabillasabbaha on 1/4/18.
 */

public class GetBalanceTokoCashUseCase extends UseCase<BalanceTokoCash> {

    private QrPaymentRepository repository;

    public GetBalanceTokoCashUseCase(QrPaymentRepository repository) {
        this.repository = repository;
    }

    @Override
    public Observable<BalanceTokoCash> createObservable(RequestParams requestParams) {
        return repository.getBalanceTokoCash(requestParams.getParameters());
    }
}
