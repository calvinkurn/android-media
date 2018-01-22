package com.tokopedia.mitratoppers.dashboard.interactor;

import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by nakama on 22/01/18.
 */

public class PreApproveBalanceUseCase extends UseCase {

    @Inject
    public PreApproveBalanceUseCase(){

    }

    @Override
    public Observable createObservable(RequestParams requestParams) {
        return null;
    }
}
