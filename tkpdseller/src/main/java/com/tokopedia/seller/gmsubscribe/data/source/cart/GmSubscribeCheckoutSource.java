package com.tokopedia.seller.gmsubscribe.data.source.cart;

import com.google.gson.Gson;
import com.tokopedia.seller.gmsubscribe.data.mapper.GmSubscribeCheckoutMapper;
import com.tokopedia.seller.gmsubscribe.data.source.cart.cloud.GmSubscribeCartCloud;
import com.tokopedia.seller.gmsubscribe.data.source.cart.cloud.model.checkout.GmCheckoutServiceModel;
import com.tokopedia.seller.gmsubscribe.domain.cart.model.GmCheckoutDomainModel;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by sebastianuskh on 2/3/17.
 */
public class GmSubscribeCheckoutSource {
    private final GmSubscribeCartCloud cartCloud;
    private final Gson gson;
    private final GmSubscribeCheckoutMapper mapper;

    public GmSubscribeCheckoutSource(GmSubscribeCartCloud cartCloud, Gson gson, GmSubscribeCheckoutMapper mapper) {
        this.cartCloud = cartCloud;
        this.gson = gson;
        this.mapper = mapper;
    }

    public Observable<GmCheckoutDomainModel> checkoutGMSubscribe(Integer selectedProduct, Integer autoExtendSelectedProduct, String voucherCode) {
        return cartCloud.checkoutGMSubscribe(selectedProduct, autoExtendSelectedProduct, voucherCode)
                .flatMap(new ConvertToObject());
    }

    private class ConvertToObject implements Func1<String, Observable<GmCheckoutDomainModel>> {
        @Override
        public Observable<GmCheckoutDomainModel> call(String s) {
            return Observable.just(gson.fromJson(s, GmCheckoutServiceModel.class)).map(mapper);
        }
    }
}
