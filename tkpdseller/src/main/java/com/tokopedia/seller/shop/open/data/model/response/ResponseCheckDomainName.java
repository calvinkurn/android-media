package com.tokopedia.seller.shop.open.data.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by zulfikarrahman on 11/4/16.
 */

public class ResponseCheckDomainName {

    @SerializedName("shop_domain")
    @Expose
    private String domainName = null;

    @SerializedName("shop_domain_status")
    @Expose
    private String domainStatus = null;

    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    public String getDomainStatus() {
        return domainStatus;
    }

    public void setDomainStatus(String domainStatus) {
        this.domainStatus = domainStatus;
    }
}
