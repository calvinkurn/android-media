package com.tokopedia.topads.sdk.listener;

import com.tokopedia.topads.sdk.domain.model.Data;
import com.tokopedia.topads.sdk.domain.model.Shop;

/**
 * @author by errysuprayogi on 3/31/17.
 */

public interface LocalAdsClickListener {

    void onShopItemClicked(int position, Data data);

    void onProductItemClicked(int position, Data data);

    void onAddFavorite(int position, Shop shop);

}
