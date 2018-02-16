package com.tokopedia.mitratoppers.preapprove.domain.interactor;

import com.tokopedia.mitratoppers.common.domain.MitraToppersRepository;
import com.tokopedia.mitratoppers.preapprove.data.model.response.preapprove.ResponsePreApprove;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by nakama on 22/01/18.
 */

public class PreApproveBalanceUseCase extends UseCase<ResponsePreApprove> {

    private final MitraToppersRepository mitraToppersRepository;

    @Inject
    public PreApproveBalanceUseCase(MitraToppersRepository mitraToppersRepository){
        this.mitraToppersRepository = mitraToppersRepository;
    }

    @Override
    public Observable<ResponsePreApprove> createObservable(RequestParams requestParams) {
        return mitraToppersRepository.getPreApproveBalance();
    }
}
