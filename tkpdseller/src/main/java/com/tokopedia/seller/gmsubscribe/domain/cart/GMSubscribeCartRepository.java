package com.tokopedia.seller.gmsubscribe.domain.cart;

import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.seller.gmsubscribe.domain.cart.model.GMCheckoutDomainModel;
import com.tokopedia.seller.gmsubscribe.domain.cart.model.GMVoucherCheckDomainModel;

import rx.Observable;

/**
 * Created by sebastianuskh on 2/3/17.
 */
public interface GMSubscribeCartRepository {
    Observable<GMVoucherCheckDomainModel> checkVoucher(Integer selectedProduct, String voucherCode);

    Observable<GMCheckoutDomainModel> checkoutGMSubscribe(Integer selectedProduct, Integer autoExtendSelectedProduct, String voucherCode);
}
