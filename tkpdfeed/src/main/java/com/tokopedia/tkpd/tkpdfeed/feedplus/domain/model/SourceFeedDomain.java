package com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model;

import javax.annotation.Nullable;

/**
 * @author ricoharisin .
 */

public class SourceFeedDomain {
    private final @Nullable
    Integer type;

    private final @Nullable
    ShopFeedDomain shop;

    public SourceFeedDomain(@Nullable Integer type, @Nullable ShopFeedDomain shop) {
        this.type = type;
        this.shop = shop;
    }

    @Nullable
    public Integer getType() {
        return type;
    }

    @Nullable
    public ShopFeedDomain getShop() {
        return shop;
    }
}
