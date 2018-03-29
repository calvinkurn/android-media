
package com.tokopedia.tkpdstream.chatroom.domain.pojo.channelinfo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Flashsale {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("app_link")
    @Expose
    private String appLink;
    @SerializedName("campaign_name")
    @Expose
    private String campaignName;
    @SerializedName("campaign_short_name")
    @Expose
    private String campaignShortName;
    @SerializedName("start_date")
    @Expose
    private long startDate;
    @SerializedName("end_date")
    @Expose
    private long endDate;
    @SerializedName("products")
    @Expose
    private List<Product> products = null;

    public String getStatus() {
        return status;
    }

    public String getAppLink() {
        return appLink;
    }

    public String getCampaignName() {
        return campaignName;
    }

    public String getCampaignShortName() {
        return campaignShortName;
    }

    public long getStartDate() {
        return startDate;
    }

    public long getEndDate() {
        return endDate;
    }

    public List<Product> getProducts() {
        return products;
    }

}
