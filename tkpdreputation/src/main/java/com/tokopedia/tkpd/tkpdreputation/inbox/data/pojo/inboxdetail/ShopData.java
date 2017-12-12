
package com.tokopedia.tkpd.tkpdreputation.inbox.data.pojo.inboxdetail;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ShopData {

    @SerializedName("shop_id")
    @Expose
    private int shopId;
    @SerializedName("shop_user_id")
    @Expose
    private int shopUserId;
    @SerializedName("domain")
    @Expose
    private String domain;
    @SerializedName("shop_name")
    @Expose
    private String shopName;
    @SerializedName("shop_url")
    @Expose
    private String shopUrl;
    @SerializedName("logo")
    @Expose
    private String logo;
    @SerializedName("shop_reputation")
    @Expose
    private ShopReputation shopReputation;

    public int getShopId() {
        return shopId;
    }

    public void setShopId(int shopId) {
        this.shopId = shopId;
    }

    public int getShopUserId() {
        return shopUserId;
    }

    public void setShopUserId(int shopUserId) {
        this.shopUserId = shopUserId;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getShopUrl() {
        return shopUrl;
    }

    public void setShopUrl(String shopUrl) {
        this.shopUrl = shopUrl;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public ShopReputation getShopReputation() {
        return shopReputation;
    }

    public void setShopReputation(ShopReputation shopReputation) {
        this.shopReputation = shopReputation;
    }

}
