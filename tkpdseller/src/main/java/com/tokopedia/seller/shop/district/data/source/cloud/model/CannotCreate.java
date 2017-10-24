
package com.tokopedia.seller.shop.district.data.source.cloud.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CannotCreate {

    @SerializedName("shop_name")
    @Expose
    private String shopName;
    @SerializedName("domain")
    @Expose
    private String domain;

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

}
