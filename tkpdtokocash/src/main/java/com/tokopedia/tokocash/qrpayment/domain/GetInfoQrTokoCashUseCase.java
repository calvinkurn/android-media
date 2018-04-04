package com.tokopedia.tokocash.qrpayment.domain;

import com.tokopedia.tokocash.qrpayment.presentation.model.InfoQrTokoCash;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import rx.Observable;

/**
 * Created by nabillasabbaha on 1/2/18.
 */

public class GetInfoQrTokoCashUseCase extends UseCase<InfoQrTokoCash> {

    public static final String IDENTIFIER = "identifier";

    private IQrPaymentRepository repository;

    public GetInfoQrTokoCashUseCase(IQrPaymentRepository repository) {
        this.repository = repository;
    }

    @Override
    public Observable<InfoQrTokoCash> createObservable(RequestParams requestParams) {
        return repository.getInfoQrTokoCash(requestParams.getParameters());
    }
}
