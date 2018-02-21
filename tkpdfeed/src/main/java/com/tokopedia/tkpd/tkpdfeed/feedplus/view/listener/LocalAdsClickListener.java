package com.tokopedia.tkpd.tkpdfeed.feedplus.view.listener;

import com.tokopedia.topads.sdk.domain.model.Data;

/**
 * Created by milhamj on 18/01/18.
 */

public interface LocalAdsClickListener {

    void onShopItemClicked(int position, Data data);

    void onProductItemClicked(int position, Data data);

    void onAddFavorite(int position, Data dataShop);

    void onInfoClicked();
}
