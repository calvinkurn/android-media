package com.tokopedia.home.explore.domain.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by errysuprayogi on 2/8/18.
 */
public class ShopData {
    @SerializedName("shop_id")
    private int shopId;
    @SerializedName("shop_name")
    private String shopName;
    @SerializedName("is_gold")
    private int isGold;
    @SerializedName("is_gold_badge")
    private boolean isGoldBadge;
    @SerializedName("is_official")
    private int isOfficial;
    @SerializedName("logo")
    private String logo;
    @SerializedName("reputation_badge")
    private String reputationBadge;
    @SerializedName("s_badges")
    private List<?> sBadges;

    public int getShopId() {
        return shopId;
    }

    public void setShopId(int shopId) {
        this.shopId = shopId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public int getIsGold() {
        return isGold;
    }

    public void setIsGold(int isGold) {
        this.isGold = isGold;
    }

    public boolean isIsGoldBadge() {
        return isGoldBadge;
    }

    public void setIsGoldBadge(boolean isGoldBadge) {
        this.isGoldBadge = isGoldBadge;
    }

    public int getIsOfficial() {
        return isOfficial;
    }

    public void setIsOfficial(int isOfficial) {
        this.isOfficial = isOfficial;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getReputationBadge() {
        return reputationBadge;
    }

    public void setReputationBadge(String reputationBadge) {
        this.reputationBadge = reputationBadge;
    }

    public List<?> getSBadges() {
        return sBadges;
    }

    public void setSBadges(List<?> sBadges) {
        this.sBadges = sBadges;
    }
}
