package com.tokopedia.seller.gmsubscribe.data.source.cart;

import com.google.gson.Gson;
import com.tokopedia.seller.gmsubscribe.data.mapper.GMSubscribeCheckoutMapper;
import com.tokopedia.seller.gmsubscribe.data.source.cart.cloud.GMSubscribeCartCloud;
import com.tokopedia.seller.gmsubscribe.data.source.cart.cloud.model.checkout.GMCheckoutServiceModel;
import com.tokopedia.seller.gmsubscribe.domain.cart.model.GMCheckoutDomainModel;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by sebastianuskh on 2/3/17.
 */
public class GMSubscribeCheckoutSource {
    private final GMSubscribeCartCloud cartCloud;
    private final Gson gson;
    private final GMSubscribeCheckoutMapper mapper;

    public GMSubscribeCheckoutSource(GMSubscribeCartCloud cartCloud, Gson gson, GMSubscribeCheckoutMapper mapper) {
        this.cartCloud = cartCloud;
        this.gson = gson;
        this.mapper = mapper;
    }

    public Observable<GMCheckoutDomainModel> checkoutGMSubscribe(Integer selectedProduct, Integer autoExtendSelectedProduct, String voucherCode) {
        return cartCloud.checkoutGMSubscribe(selectedProduct, autoExtendSelectedProduct, voucherCode)
                .flatMap(new ConvertToObject());
    }

    private class ConvertToObject implements Func1<String, Observable<GMCheckoutDomainModel>> {
        @Override
        public Observable<GMCheckoutDomainModel> call(String s) {
            return Observable.just(gson.fromJson(s, GMCheckoutServiceModel.class)).map(mapper);
        }
    }
}
