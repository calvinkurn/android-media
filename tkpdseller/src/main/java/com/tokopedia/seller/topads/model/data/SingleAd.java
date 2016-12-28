package com.tokopedia.seller.topads.model.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class SingleAd implements Ad {

    @SerializedName("ad_id")
    @Expose
    private int id;
    @SerializedName("ad_status")
    @Expose
    private int status;
    @SerializedName("ad_status_desc")
    @Expose
    private String statusDesc;
    @SerializedName("ad_status_toogle")
    @Expose
    private int statusToogle;
    @SerializedName("ad_price_bid_fmt")
    @Expose
    private String priceBidFmt;
    @SerializedName("ad_price_daily_fmt")
    @Expose
    private String priceDailyFmt;
    @SerializedName("ad_price_daily_spent_fmt")
    @Expose
    private String priceDailySpentFmt;
    @SerializedName("ad_price_daily_bar")
    @Expose
    private String priceDailyBar;
    @SerializedName("ad_editable")
    @Expose
    private int editable;
    @SerializedName("ad_start_date")
    @Expose
    private String startDate;
    @SerializedName("ad_start_time")
    @Expose
    private String startTime;
    @SerializedName("ad_end_date")
    @Expose
    private String endDate;
    @SerializedName("ad_end_time")
    @Expose
    private String endTime;

    @SerializedName("stat_avg_click")
    @Expose
    private String statAvgClick;
    @SerializedName("stat_total_spent")
    @Expose
    private String statTotalSpent;
    @SerializedName("stat_total_impression")
    @Expose
    private String statTotalImpression;
    @SerializedName("stat_total_click")
    @Expose
    private String statTotalClick;
    @SerializedName("stat_total_ctr")
    @Expose
    private String statTotalCtr;
    @SerializedName("stat_total_conversion")
    @Expose
    private String statTotalConversion;
    @SerializedName("label_edit")
    @Expose
    private String labelEdit;
    @SerializedName("label_per_click")
    @Expose
    private String labelPerClick;
    @SerializedName("label_of")
    @Expose
    private String labelOf;

    @SerializedName("product_name")
    @Expose
    private String name;

    @SerializedName("ad_moderated")
    @Expose
    private int adModerated;
    @SerializedName("ad_moderated_reason")
    @Expose
    private String adModeratedReason;

    @SerializedName("item_id")
    @Expose
    private int itemId;
    @SerializedName("product_uri")
    @Expose
    private String productUri;
    @SerializedName("product_active")
    @Expose
    private int productActive;
    @SerializedName("group_name")
    @Expose
    private String groupName;
    @SerializedName("group_id")
    @Expose
    private int groupId;

    @Override
    public String getStatTotalImpression() {
        return statTotalImpression;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public int getStatus() {
        return status;
    }

    @Override
    public String getStatusDesc() {
        return statusDesc;
    }

    @Override
    public int getStatusToogle() {
        return statusToogle;
    }

    @Override
    public String getPriceBidFmt() {
        return priceBidFmt;
    }

    @Override
    public String getPriceDailyFmt() {
        return priceDailyFmt;
    }

    @Override
    public String getPriceDailySpentFmt() {
        return priceDailySpentFmt;
    }

    @Override
    public String getPriceDailyBar() {
        return priceDailyBar;
    }

    public int getEditable() {
        return editable;
    }

    @Override
    public String getStartDate() {
        return startDate;
    }

    @Override
    public String getStartTime() {
        return startTime;
    }

    @Override
    public String getEndDate() {
        return endDate;
    }

    @Override
    public String getEndTime() {
        return endTime;
    }

    @Override
    public String getStatAvgClick() {
        return statAvgClick;
    }

    @Override
    public String getStatTotalSpent() {
        return statTotalSpent;
    }

    @Override
    public String getStatTotalClick() {
        return statTotalClick;
    }

    @Override
    public String getStatTotalCtr() {
        return statTotalCtr;
    }

    @Override
    public String getStatTotalConversion() {
        return statTotalConversion;
    }

    @Override
    public String getLabelEdit() {
        return labelEdit;
    }

    @Override
    public String getLabelPerClick() {
        return labelPerClick;
    }

    @Override
    public String getLabelOf() {
        return labelOf;
    }

    @Override
    public String getName() {
        return name;
    }

    public int getAdModerated() {
        return adModerated;
    }

    public String getAdModeratedReason() {
        return adModeratedReason;
    }

    public int getItemId() {
        return itemId;
    }

    public String getProductUri() {
        return productUri;
    }

    public int getProductActive() {
        return productActive;
    }

    public String getGroupName() {
        return groupName;
    }

    public int getGroupId() {
        return groupId;
    }
}