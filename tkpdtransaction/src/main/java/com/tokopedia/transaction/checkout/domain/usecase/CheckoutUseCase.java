package com.tokopedia.transaction.checkout.domain.usecase;

import com.tokopedia.transaction.checkout.view.data.cartcheckout.CheckoutData;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import rx.Observable;

/**
 * @author anggaprasetiyo on 26/02/18.
 */

public class CheckoutUseCase extends UseCase<CheckoutData> {
    @Override
    public Observable<CheckoutData> createObservable(RequestParams requestParams) {
        return null;
    }
}
