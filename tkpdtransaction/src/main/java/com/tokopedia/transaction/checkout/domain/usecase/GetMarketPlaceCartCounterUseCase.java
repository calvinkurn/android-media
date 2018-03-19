package com.tokopedia.transaction.checkout.domain.usecase;

import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import rx.Observable;

/**
 * @author anggaprasetiyo on 16/03/18.
 */

public class GetMarketPlaceCartCounterUseCase extends UseCase<Integer> {


    @Override
    public Observable<Integer> createObservable(RequestParams requestParams) {
        return null;
    }
}
