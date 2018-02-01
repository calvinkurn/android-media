
package com.tokopedia.seller.product.edit.view.model.edit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductShopViewModel {

    @SerializedName("shop_id")
    @Expose
    private long shopId;
    @SerializedName("shop_name")
    @Expose
    private String shopName;
    @SerializedName("shop_domain")
    @Expose
    private String shopDomain;
    @SerializedName("shop_url")
    @Expose
    private String shopUrl;

    public long getShopId() {
        return shopId;
    }

    public void setShopId(long shopId) {
        this.shopId = shopId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getShopDomain() {
        return shopDomain;
    }

    public void setShopDomain(String shopDomain) {
        this.shopDomain = shopDomain;
    }

    public String getShopUrl() {
        return shopUrl;
    }

    public void setShopUrl(String shopUrl) {
        this.shopUrl = shopUrl;
    }

}
