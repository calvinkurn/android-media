package com.tokopedia.tokocash.activation.domain;

import com.tokopedia.tokocash.activation.data.ActivateRepository;
import com.tokopedia.tokocash.activation.presentation.model.ActivateTokoCashData;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import rx.Observable;

/**
 * Created by nabillasabbaha on 2/1/18.
 */

public class RequestOtpTokoCashUseCase extends UseCase<ActivateTokoCashData> {

    private ActivateRepository repository;

    public RequestOtpTokoCashUseCase(ActivateRepository repository) {
        this.repository = repository;
    }

    @Override
    public Observable<ActivateTokoCashData> createObservable(RequestParams requestParams) {
        return repository.requestOTPWallet();
    }
}
