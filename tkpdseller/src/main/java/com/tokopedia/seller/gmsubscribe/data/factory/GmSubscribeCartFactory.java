package com.tokopedia.seller.gmsubscribe.data.factory;

import com.tokopedia.seller.gmsubscribe.data.mapper.GmSubscribeCheckoutMapper;
import com.tokopedia.seller.gmsubscribe.data.mapper.cart.GmSubscribeVoucherMapper;
import com.tokopedia.seller.gmsubscribe.data.source.cart.GmSubscribeCheckoutSource;
import com.tokopedia.seller.gmsubscribe.data.source.cart.GmSubscribeVoucherSource;
import com.tokopedia.seller.gmsubscribe.data.source.cart.cloud.GmSubscribeCartCloud;

/**
 * Created by sebastianuskh on 2/3/17.
 */
public class GmSubscribeCartFactory {
    private final GmSubscribeCartCloud cartCloud;
    private final GmSubscribeVoucherMapper voucherMapper;
    private final GmSubscribeCheckoutMapper checkoutMapper;

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
