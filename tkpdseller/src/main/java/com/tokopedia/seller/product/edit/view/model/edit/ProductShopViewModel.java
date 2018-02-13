
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

    public String getShopName() {
        return shopName;
    }

    public String getShopDomain() {
        return shopDomain;
    }

    public String getShopUrl() {
        return shopUrl;
    }

}
