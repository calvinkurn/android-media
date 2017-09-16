package com.tokopedia.gm.subscribe.data.repository;

import com.tokopedia.gm.subscribe.data.factory.GmSubscribeCartFactory;
import com.tokopedia.gm.subscribe.data.source.cart.GmSubscribeCheckoutSource;
import com.tokopedia.gm.subscribe.data.source.cart.GmSubscribeVoucherSource;
import com.tokopedia.gm.subscribe.domain.cart.GmSubscribeCartRepository;
import com.tokopedia.gm.subscribe.domain.cart.model.GmCheckoutDomainModel;
import com.tokopedia.gm.subscribe.domain.cart.model.GmVoucherCheckDomainModel;

import rx.Observable;

/**
 * @author sebastianuskh on 2/3/17.
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
