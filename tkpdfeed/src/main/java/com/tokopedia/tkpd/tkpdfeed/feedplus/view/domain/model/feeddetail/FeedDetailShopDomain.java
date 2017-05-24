package com.tokopedia.tkpd.tkpdfeed.feedplus.view.domain.model.feeddetail;

import javax.annotation.Nullable;

/**
 * @author by nisie on 5/24/17.
 */

public class FeedDetailShopDomain {

    private final @Nullable
    Integer id;

    private final @Nullable String name;

    private final @Nullable String avatar;

    private final @Nullable Boolean isOfficial;

    private final @Nullable Boolean isGold;

    private final @Nullable String url;

    private final @Nullable String shopLink;

    private final @Nullable String shareLinkDescription;

    private final @Nullable String shareLinkURL;

    public FeedDetailShopDomain(Integer id,
                                String name,
                                String avatar,
                                Boolean isOfficial,
                                Boolean isGold,
                                String url,
                                String shopLink,
                                String shareLinkDescription,
                                String shareLinkURL) {
        this.id = id;
        this.name = name;
        this.avatar = avatar;
        this.isOfficial = isOfficial;
        this.isGold = isGold;
        this.url = url;
        this.shopLink = shopLink;
        this.shareLinkDescription = shareLinkDescription;
        this.shareLinkURL = shareLinkURL;
    }
}
