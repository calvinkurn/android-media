package com.tokopedia.tkpd.tkpdfeed.feedplus.view.domain.model;

import javax.annotation.Nullable;

/**
 * @author ricoharisin .
 */

public class ShopFeedDomain {

    private @Nullable
    Integer id;

    private @Nullable String name;

    private @Nullable String avatar;

    private @Nullable Boolean isOfficial;

    private @Nullable Boolean isGold;

    private @Nullable Object url;

    private @Nullable String shopLink;

    public ShopFeedDomain(@Nullable Integer id, @Nullable String name, @Nullable String avatar,
                @Nullable Boolean isOfficial, @Nullable Boolean isGold, @Nullable Object url,
                @Nullable String shopLink) {
        this.id = id;
        this.name = name;
        this.avatar = avatar;
        this.isOfficial = isOfficial;
        this.isGold = isGold;
        this.url = url;
        this.shopLink = shopLink;
    }

    @Nullable
    public Integer getId() {
        return id;
    }

    public void setId(@Nullable Integer id) {
        this.id = id;
    }

    @Nullable
    public String getName() {
        return name;
    }

    public void setName(@Nullable String name) {
        this.name = name;
    }

    @Nullable
    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(@Nullable String avatar) {
        this.avatar = avatar;
    }

    @Nullable
    public Boolean getOfficial() {
        return isOfficial;
    }

    public void setOfficial(@Nullable Boolean official) {
        isOfficial = official;
    }

    @Nullable
    public Boolean getGold() {
        return isGold;
    }

    public void setGold(@Nullable Boolean gold) {
        isGold = gold;
    }

    @Nullable
    public Object getUrl() {
        return url;
    }

    public void setUrl(@Nullable Object url) {
        this.url = url;
    }

    @Nullable
    public String getShopLink() {
        return shopLink;
    }

    public void setShopLink(@Nullable String shopLink) {
        this.shopLink = shopLink;
    }
}
