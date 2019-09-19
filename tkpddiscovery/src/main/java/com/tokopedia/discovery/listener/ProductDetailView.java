package com.tokopedia.discovery.listener;

import com.tokopedia.core.product.listener.ViewListener;

public interface ProductDetailView extends ViewListener {

    /**
     * Pada saat rating product diklik
     */
    void onProductRatingClicked(String productId, String shopId, String productName);

}
