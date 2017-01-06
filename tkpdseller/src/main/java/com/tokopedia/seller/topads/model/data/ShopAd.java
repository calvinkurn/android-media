package com.tokopedia.seller.topads.model.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.seller.topads.constant.TopAdsConstant;

/**
 * Created by Nathaniel on 12/28/2016.
 */

public class ShopAd implements Ad {

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

    @SerializedName("shop_name")
    @Expose
    private String name;

    @SerializedName("shop_id")
    @Expose
    private int shopId;
    @SerializedName("shop_uri")
    @Expose
    private String shopUri;

    @Override
    public String getPriceDailyFmt() {
        return priceDailyFmt;
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
    public String getStatTotalImpression() {
        return statTotalImpression;
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

    public int getShopId() {
        return shopId;
    }

    public String getShopUri() {
        return shopUri;
    }

    public boolean isStatusActive() {
        switch (status) {
            case TopAdsConstant.STATUS_AD_ACTIVE:
                return true;
            case TopAdsConstant.STATUS_AD_NOT_ACTIVE:
            case TopAdsConstant.STATUS_AD_NOT_SENT:
                break;
        }
        return false;
    }
}
