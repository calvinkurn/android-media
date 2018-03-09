package com.tokopedia.tokocash.activation.domain;

import com.tokopedia.tokocash.activation.data.ActivateRepository;
import com.tokopedia.tokocash.activation.presentation.model.ActivateTokoCashData;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import rx.Observable;

/**
 * Created by nabillasabbaha on 2/1/18.
 */

public class LinkedTokoCashUseCase extends UseCase<ActivateTokoCashData> {

    public static final String OTP_CODE = "otp";

    private ActivateRepository repository;

    public LinkedTokoCashUseCase(ActivateRepository repository) {
        this.repository = repository;
    }

    @Override
    public Observable<ActivateTokoCashData> createObservable(RequestParams requestParams) {
        return repository.linkedWalletToTokoCash(requestParams.getParamsAllValueInString());
    }
}
