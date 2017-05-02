package com.tokopedia.topads.sdk.listener;

import com.tokopedia.topads.sdk.domain.model.Data;
import com.tokopedia.topads.sdk.domain.model.Product;
import com.tokopedia.topads.sdk.domain.model.Shop;

/**
 * @author by errysuprayogi on 3/29/17.
 */

public interface TopAdsItemClickListener {

    void onProductItemClicked(Product product);

    void onShopItemClicked(Shop shop);

    void onAddFavorite(Shop shop);

}
