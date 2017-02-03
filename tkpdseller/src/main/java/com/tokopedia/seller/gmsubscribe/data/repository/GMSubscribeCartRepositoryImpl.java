package com.tokopedia.seller.gmsubscribe.data.repository;

import com.tokopedia.seller.gmsubscribe.data.factory.GMSubscribeCartFactory;
import com.tokopedia.seller.gmsubscribe.data.source.cart.GMSubscribeCheckoutSource;
import com.tokopedia.seller.gmsubscribe.data.source.cart.GMSubscribeVoucherSource;
import com.tokopedia.seller.gmsubscribe.domain.cart.GMSubscribeCartRepository;
import com.tokopedia.seller.gmsubscribe.domain.cart.model.GMCheckoutDomainModel;
import com.tokopedia.seller.gmsubscribe.domain.cart.model.GMVoucherCheckDomainModel;

import rx.Observable;

/**
 * Created by sebastianuskh on 2/3/17.
 */

public class GMSubscribeCartRepositoryImpl implements GMSubscribeCartRepository {
    private GMSubscribeCartFactory gmSubscribeCartFactory;

    @Override
    public Observable<GMVoucherCheckDomainModel> checkVoucher(Integer selectedProduct, String voucherCode) {
        GMSubscribeVoucherSource voucherSource = gmSubscribeCartFactory.createVoucherSource();
        return voucherSource.checkVoucher(selectedProduct, voucherCode);
    }

    @Override
    public Observable<GMCheckoutDomainModel> checkoutGMSubscribe(Integer selectedProduct, Integer autoExtendSelectedProduct, String voucherCode) {
        GMSubscribeCheckoutSource checkoutSource = gmSubscribeCartFactory.createCheckoutSource();
        return checkoutSource.checkoutGMSubscribe(selectedProduct, autoExtendSelectedProduct, voucherCode);
    }
}
