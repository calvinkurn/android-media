package com.tokopedia.seller.gmsubscribe.data.source.cart;

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
    private final GmSubscribeCheckoutMapper mapper;

    public GmSubscribeCheckoutSource(GmSubscribeCartCloud cartCloud, GmSubscribeCheckoutMapper mapper) {
        this.cartCloud = cartCloud;
        this.mapper = mapper;
    }

    public Observable<GmCheckoutDomainModel> checkoutGMSubscribe(Integer selectedProduct, Integer autoExtendSelectedProduct, String voucherCode) {
        return cartCloud.checkoutGMSubscribe(selectedProduct, autoExtendSelectedProduct, voucherCode)
                .flatMap(new ConvertToObject());
    }

    private class ConvertToObject implements Func1<GmCheckoutServiceModel, Observable<GmCheckoutDomainModel>> {
        @Override
        public Observable<GmCheckoutDomainModel> call(GmCheckoutServiceModel response) {
            return Observable.just(response).map(mapper);
        }
    }
}
