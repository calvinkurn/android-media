package com.tokopedia.seller.gmsubscribe.domain.cart;

import com.tokopedia.seller.gmsubscribe.domain.cart.model.GmCheckoutDomainModel;
import com.tokopedia.seller.gmsubscribe.domain.cart.model.GmVoucherCheckDomainModel;

import rx.Observable;

/**
 * Created by sebastianuskh on 2/3/17.
 */
public interface GmSubscribeCartRepository {
    Observable<GmVoucherCheckDomainModel> checkVoucher(Integer selectedProduct, String voucherCode);

    Observable<GmCheckoutDomainModel> checkoutGMSubscribe(Integer selectedProduct, Integer autoExtendSelectedProduct, String voucherCode);
}
