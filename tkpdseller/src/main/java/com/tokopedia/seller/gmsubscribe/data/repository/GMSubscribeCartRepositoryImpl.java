package com.tokopedia.seller.gmsubscribe.data.repository;

import com.tokopedia.seller.gmsubscribe.data.factory.GmSubscribeCartFactory;
import com.tokopedia.seller.gmsubscribe.data.source.cart.GmSubscribeCheckoutSource;
import com.tokopedia.seller.gmsubscribe.data.source.cart.GmSubscribeVoucherSource;
import com.tokopedia.seller.gmsubscribe.domain.cart.GmSubscribeCartRepository;
import com.tokopedia.seller.gmsubscribe.domain.cart.model.GmCheckoutDomainModel;
import com.tokopedia.seller.gmsubscribe.domain.cart.model.GmVoucherCheckDomainModel;

import rx.Observable;

/**
 * Created by sebastianuskh on 2/3/17.
 */

public class GmSubscribeCartRepositoryImpl implements GmSubscribeCartRepository {
    private final GmSubscribeCartFactory gmSubscribeCartFactory;

    public GmSubscribeCartRepositoryImpl(GmSubscribeCartFactory gmSubscribeCartFactory) {
        this.gmSubscribeCartFactory = gmSubscribeCartFactory;
    }

    @Override
    public Observable<GmVoucherCheckDomainModel> checkVoucher(Integer selectedProduct, String voucherCode) {
        GmSubscribeVoucherSource voucherSource = gmSubscribeCartFactory.createVoucherSource();
        return voucherSource.checkVoucher(selectedProduct, voucherCode);
    }

    @Override
    public Observable<GmCheckoutDomainModel> checkoutGMSubscribe(Integer selectedProduct, Integer autoExtendSelectedProduct, String voucherCode) {
        GmSubscribeCheckoutSource checkoutSource = gmSubscribeCartFactory.createCheckoutSource();
        return checkoutSource.checkoutGMSubscribe(selectedProduct, autoExtendSelectedProduct, voucherCode);
    }
}
