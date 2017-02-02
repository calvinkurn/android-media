package com.tokopedia.seller.gmsubscribe.data.factory;

import com.tokopedia.seller.gmsubscribe.data.source.product.GMSubscribeProductListSource;
import com.tokopedia.seller.gmsubscribe.data.source.product.GMSubscribeProductSource;

/**
 * Created by sebastianuskh on 2/2/17.
 */
public class GMSubscribeProductFactory {

    private final GMSubscribeProductListSource gmSubscribeProductListSource;

    public GMSubscribeProductFactory(GMSubscribeProductListSource gmSubscribeProductListSource) {
        this.gmSubscribeProductListSource = gmSubscribeProductListSource;
    }

    public GMSubscribeProductSource createGMSubscribeProductSource() {
        return new GMSubscribeProductSource(gmSubscribeProductListSource);
    }
}
