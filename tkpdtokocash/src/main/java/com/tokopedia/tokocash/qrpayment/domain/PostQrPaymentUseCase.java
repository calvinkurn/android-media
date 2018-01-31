package com.tokopedia.tokocash.qrpayment.domain;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.tokocash.qrpayment.data.repository.QrPaymentRepository;
import com.tokopedia.tokocash.qrpayment.presentation.model.QrPaymentTokoCash;

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

    public PostQrPaymentUseCase(ThreadExecutor threadExecutor,
                                PostExecutionThread postExecutionThread,
                                QrPaymentRepository repository) {
        super(threadExecutor, postExecutionThread);
        this.repository = repository;
    }

    @Override
    public Observable<QrPaymentTokoCash> createObservable(RequestParams requestParams) {
        return repository.postQrPayment(requestParams.getParameters());
    }
}
