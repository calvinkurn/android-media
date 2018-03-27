
package com.tokopedia.tkpdstream.chatroom.domain.pojo.sprintsale;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UpcomingFlashsale {

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

    public String getCampaignName() {
        return campaignName;
    }

    public void setCampaignName(String campaignName) {
        this.campaignName = campaignName;
    }

    public String getCampaignShortName() {
        return campaignShortName;
    }

    public void setCampaignShortName(String campaignShortName) {
        this.campaignShortName = campaignShortName;
    }

    public long getStartDate() {
        return startDate;
    }

    public void setStartDate(long startDate) {
        this.startDate = startDate;
    }

    public long getEndDate() {
        return endDate;
    }

    public void setEndDate(long endDate) {
        this.endDate = endDate;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

}
