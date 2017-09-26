package com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.inboxdetail;

/**
 * @author by nisie on 9/26/17.
 */

public class ShopFavoritedDomain {
    boolean isShopFavorited;

    public ShopFavoritedDomain(boolean isShopFavorited) {
        this.isShopFavorited = isShopFavorited;
    }

    public boolean isShopFavorited() {
        return isShopFavorited;
    }
}
