package com.tokopedia.transaction.checkout.domain;

import com.google.gson.Gson;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.core.router.transactionmodule.sharedata.AddToCartRequest;
import com.tokopedia.transaction.checkout.domain.response.addtocart.AddToCartDataResponse;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author anggaprasetiyo on 13/02/18.
 */

public class AddToCartUseCase extends UseCase<AddToCartDataResponse> {
    public static final String PARAM_ADD_TO_CART = "PARAM_ADD_TO_CART";

    private static final String KEY_PARAM_PARAMS = "params";
    private final Gson gson;

    private ICartRepository iCartRepository;

    @Inject
    public AddToCartUseCase(ICartRepository iCartRepository, Gson gson) {
        this.iCartRepository = iCartRepository;
        this.gson = gson;
    }

    @Override
    public Observable<AddToCartDataResponse> createObservable(RequestParams requestParams) {
        AddToCartRequest addToCartRequest = (AddToCartRequest) requestParams.getObject(PARAM_ADD_TO_CART);
        TKPDMapParam<String, String> paramRequest = new TKPDMapParam<>();
        paramRequest.put(KEY_PARAM_PARAMS, gson.toJson(addToCartRequest));
        return iCartRepository.addToCartData(paramRequest);
    }

}
