package com.tokopedia.transaction.checkout.domain.usecase;

import android.content.Context;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.transaction.checkout.domain.ICartRepository;
import com.tokopedia.transaction.checkout.domain.response.shippingaddress.ShippingAddressDataResponse;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;


import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by kris on 2/28/18. Tokopedia
 */

public class SubmitMultipleAddressUseCase extends UseCase<Boolean>{

    private ICartRepository repository;

    @Inject
    public SubmitMultipleAddressUseCase(ICartRepository repository) {
        this.repository = repository;
    }

    @Override
    public Observable<Boolean> createObservable(RequestParams requestParams) {
        TKPDMapParam<String, String> mapParam = new TKPDMapParam<>();
        mapParam.putAll(requestParams.getParamsAllValueInString());
        return repository.shippingAddress(mapParam).map(new Func1<ShippingAddressDataResponse, Boolean>() {
            @Override
            public Boolean call(ShippingAddressDataResponse shippingAddressDataResponse) {
                return shippingAddressDataResponse.getSuccess() == 1;
            }
        });
    }
}
