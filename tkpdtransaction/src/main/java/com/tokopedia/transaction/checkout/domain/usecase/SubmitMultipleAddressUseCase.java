package com.tokopedia.transaction.checkout.domain.usecase;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.transaction.checkout.data.entity.response.shippingaddress.ShippingAddressDataResponse;
import com.tokopedia.transaction.checkout.data.repository.ICartRepository;
import com.tokopedia.transaction.checkout.domain.datamodel.cartmultipleshipment.SetShippingAddressData;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by kris on 2/28/18. Tokopedia
 */

public class SubmitMultipleAddressUseCase extends UseCase<SetShippingAddressData> {

    private ICartRepository repository;

    @Inject
    public SubmitMultipleAddressUseCase(ICartRepository repository) {
        this.repository = repository;
    }

    @Override
    public Observable<SetShippingAddressData> createObservable(RequestParams requestParams) {
        TKPDMapParam<String, String> mapParam = new TKPDMapParam<>();
        mapParam.putAll(requestParams.getParamsAllValueInString());
        return repository.shippingAddress(
                AuthUtil.generateParamsNetwork(MainApplication.getAppContext(), mapParam)
        ).map(
                new Func1<ShippingAddressDataResponse, SetShippingAddressData>() {
                    @Override
                    public SetShippingAddressData call(ShippingAddressDataResponse shippingAddressDataResponse) {
                        return new SetShippingAddressData.Builder()
                                .success(shippingAddressDataResponse.getSuccess() == 1)
                                .build();
                    }
                });
    }
}
