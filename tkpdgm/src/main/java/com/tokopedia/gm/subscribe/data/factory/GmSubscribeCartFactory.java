package com.tokopedia.gm.subscribe.data.factory;

import com.tokopedia.gm.subscribe.data.mapper.GmSubscribeCheckoutMapper;
import com.tokopedia.gm.subscribe.data.mapper.cart.GmSubscribeVoucherMapper;
import com.tokopedia.gm.subscribe.data.source.cart.GmSubscribeCheckoutSource;
import com.tokopedia.gm.subscribe.data.source.cart.GmSubscribeVoucherSource;
import com.tokopedia.gm.subscribe.data.source.cart.cloud.GmSubscribeCartCloud;

import javax.inject.Inject;

/**
 * @author sebastianuskh on 2/3/17.
 */
public class GmSubscribeCartFactory {
    private final GmSubscribeCartCloud cartCloud;
    private final GmSubscribeVoucherMapper voucherMapper;
    private final GmSubscribeCheckoutMapper checkoutMapper;

    @Inject
    public GmSubscribeCartFactory(GmSubscribeCartCloud cartCloud, GmSubscribeVoucherMapper voucherMapper, GmSubscribeCheckoutMapper checkoutMapper) {
        this.cartCloud = cartCloud;
        this.voucherMapper = voucherMapper;
        this.checkoutMapper = checkoutMapper;
    }

    public GmSubscribeVoucherSource createVoucherSource() {
        return new GmSubscribeVoucherSource(cartCloud, voucherMapper);
    }

    public GmSubscribeCheckoutSource createCheckoutSource() {
        return new GmSubscribeCheckoutSource(cartCloud, checkoutMapper);
    }
}
