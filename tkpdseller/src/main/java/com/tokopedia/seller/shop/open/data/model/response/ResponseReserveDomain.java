package com.tokopedia.seller.shop.open.data.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by zulfikarrahman on 11/4/16.
 */

public class ResponseReserveDomain {

    @SerializedName("shop_domain")
    @Expose
    private String shopDomain = null;

    @SerializedName("shop_name")
    @Expose
    private String shopName = null;

    @SerializedName("shop_domain_status")
    @Expose
    private String shopDomainStatus = null;

    @SerializedName("shop_name_status")
    @Expose
    private String shopNameStatus = null;

    public String getShopDomain() {
        return shopDomain;
    }

    public String getShopName() {
        return shopName;
    }

    public String getShopDomainStatus() {
        return shopDomainStatus;
    }

    public String getShopNameStatus() {
        return shopNameStatus;
    }
}
