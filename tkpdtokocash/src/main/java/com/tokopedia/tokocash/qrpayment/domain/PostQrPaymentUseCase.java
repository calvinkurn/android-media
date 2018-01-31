package com.tokopedia.tokocash.qrpayment.domain;

import com.tokopedia.tokocash.qrpayment.data.repository.QrPaymentRepository;
import com.tokopedia.tokocash.qrpayment.presentation.model.QrPaymentTokoCash;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import rx.Observable;

/**
 * Created by nabillasabbaha on 1/3/18.
 */

public class PostQrPaymentUseCase extends UseCase<QrPaymentTokoCash> {

    public static final String PREAUTH_ID = "preauth_id";
    public static final String AMOUNT = "amount";
    public static final String NOTE = "note_to_payer";
    public static final String IDENTIFIER = "merchant_identifier";

    private QrPaymentRepository repository;

    public PostQrPaymentUseCase(QrPaymentRepository repository) {
        this.repository = repository;
    }

    @Override
    public Observable<QrPaymentTokoCash> createObservable(RequestParams requestParams) {
        return repository.postQrPayment(requestParams.getParameters());
    }
}
