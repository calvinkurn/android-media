package com.tokopedia.seller.gmsubscribe.data.factory;

import com.google.gson.Gson;
import com.tokopedia.seller.gmsubscribe.data.mapper.GMSubscribeCheckoutMapper;
import com.tokopedia.seller.gmsubscribe.data.mapper.cart.GMSubscribeVoucherMapper;
import com.tokopedia.seller.gmsubscribe.data.source.cart.GMSubscribeCheckoutSource;
import com.tokopedia.seller.gmsubscribe.data.source.cart.GMSubscribeVoucherSource;
import com.tokopedia.seller.gmsubscribe.data.source.cart.cloud.GMSubscribeCartCloud;

/**
 * Created by sebastianuskh on 2/3/17.
 */
public class GMSubscribeCartFactory {
    private final GMSubscribeCartCloud cartCloud;
    private final Gson gson;
    private final GMSubscribeVoucherMapper voucherMapper;
    private final GMSubscribeCheckoutMapper checkoutMapper;

    public GMSubscribeCartFactory(GMSubscribeCartCloud cartCloud, Gson gson, GMSubscribeVoucherMapper voucherMapper, GMSubscribeCheckoutMapper checkoutMapper) {
        this.cartCloud = cartCloud;
        this.gson = gson;
        this.voucherMapper = voucherMapper;
        this.checkoutMapper = checkoutMapper;
    }

    public GMSubscribeVoucherSource createVoucherSource() {
        return new GMSubscribeVoucherSource(cartCloud, gson, voucherMapper);
    }

    public GMSubscribeCheckoutSource createCheckoutSource() {
        return new GMSubscribeCheckoutSource(cartCloud, gson, checkoutMapper);
    }
}
