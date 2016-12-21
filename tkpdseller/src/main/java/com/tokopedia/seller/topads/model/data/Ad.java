package com.tokopedia.seller.topads.model.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.core.var.RecyclerViewItem;
import com.tokopedia.core.var.TkpdState;


public class Ad extends RecyclerViewItem {
    @SerializedName("ad_id")
    @Expose
    private Integer adId;
    @SerializedName("ad_status")
    @Expose
    private Integer adStatus;
    @SerializedName("ad_status_desc")
    @Expose
    private String adStatusDesc;
    @SerializedName("ad_status_toogle")
    @Expose
    private Integer adStatusToogle;
    @SerializedName("ad_price_bid_fmt")
    @Expose
    private String adPriceBidFmt;
    @SerializedName("ad_price_daily_fmt")
    @Expose
    private String adPriceDailyFmt;
    @SerializedName("ad_price_daily_spent_fmt")
    @Expose
    private String adPriceDailySpentFmt;
    @SerializedName("ad_price_daily_bar")
    @Expose
    private String adPriceDailyBar;
    @SerializedName("ad_editable")
    @Expose
    private Integer adEditable;
    @SerializedName("ad_start_date")
    @Expose
    private String adStartDate;
    @SerializedName("ad_start_time")
    @Expose
    private String adStartTime;
    @SerializedName("ad_end_date")
    @Expose
    private String adEndDate;
    @SerializedName("ad_end_time")
    @Expose
    private String adEndTime;
    @SerializedName("shop_id")
    @Expose
    private Integer shopId;
    @SerializedName("shop_name")
    @Expose
    private String shopName;
    @SerializedName("shop_uri")
    @Expose
    private String shopUri;
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
    @SerializedName("item_id")
    @Expose
    private Integer itemId;
    @SerializedName("ad_moderated")
    @Expose
    private Integer adModerated;
    @SerializedName("ad_moderated_reason")
    @Expose
    private String adModeratedReason;
    @SerializedName("product_name")
    @Expose
    private String productName;
    @SerializedName("product_uri")
    @Expose
    private String productUri;

    public Ad(){
        setType(TkpdState.RecyclerViewItemAd.AD_TYPE);
    }

    /**
     * @return The adId
     */
    public Integer getAdId() {
        return adId;
    }

    /**
     * @param adId The ad_id
     */
    public void setAdId(Integer adId) {
        this.adId = adId;
    }

    /**
     * @return The adStatus
     */
    public Integer getAdStatus() {
        return adStatus;
    }

    /**
     * @param adStatus The ad_status
     */
    public void setAdStatus(Integer adStatus) {
        this.adStatus = adStatus;
    }

    /**
     * @return The adStatusDesc
     */
    public String getAdStatusDesc() {
        return adStatusDesc;
    }

    /**
     * @param adStatusDesc The ad_status_desc
     */
    public void setAdStatusDesc(String adStatusDesc) {
        this.adStatusDesc = adStatusDesc;
    }

    /**
     * @return The adStatusToogle
     */
    public Integer getAdStatusToogle() {
        return adStatusToogle;
    }

    /**
     * @param adStatusToogle The ad_status_toogle
     */
    public void setAdStatusToogle(Integer adStatusToogle) {
        this.adStatusToogle = adStatusToogle;
    }

    /**
     * @return The adPriceBidFmt
     */
    public String getAdPriceBidFmt() {
        return adPriceBidFmt;
    }

    /**
     * @param adPriceBidFmt The ad_price_bid_fmt
     */
    public void setAdPriceBidFmt(String adPriceBidFmt) {
        this.adPriceBidFmt = adPriceBidFmt;
    }

    /**
     * @return The adPriceDailyFmt
     */
    public String getAdPriceDailyFmt() {
        return adPriceDailyFmt;
    }

    /**
     * @param adPriceDailyFmt The ad_price_daily_fmt
     */
    public void setAdPriceDailyFmt(String adPriceDailyFmt) {
        this.adPriceDailyFmt = adPriceDailyFmt;
    }

    /**
     * @return The adPriceDailySpentFmt
     */
    public String getAdPriceDailySpentFmt() {
        return adPriceDailySpentFmt;
    }

    /**
     * @param adPriceDailySpentFmt The ad_price_daily_spent_fmt
     */
    public void setAdPriceDailySpentFmt(String adPriceDailySpentFmt) {
        this.adPriceDailySpentFmt = adPriceDailySpentFmt;
    }

    /**
     * @return The adPriceDailyBar
     */
    public String getAdPriceDailyBar() {
        return adPriceDailyBar;
    }

    /**
     * @param adPriceDailyBar The ad_price_daily_bar
     */
    public void setAdPriceDailyBar(String adPriceDailyBar) {
        this.adPriceDailyBar = adPriceDailyBar;
    }

    /**
     * @return The adEditable
     */
    public Integer getAdEditable() {
        return adEditable;
    }

    /**
     * @param adEditable The ad_editable
     */
    public void setAdEditable(Integer adEditable) {
        this.adEditable = adEditable;
    }

    /**
     * @return The adStartDate
     */
    public String getAdStartDate() {
        return adStartDate;
    }

    /**
     * @param adStartDate The ad_start_date
     */
    public void setAdStartDate(String adStartDate) {
        this.adStartDate = adStartDate;
    }

    /**
     * @return The adStartTime
     */
    public String getAdStartTime() {
        return adStartTime;
    }

    /**
     * @param adStartTime The ad_start_time
     */
    public void setAdStartTime(String adStartTime) {
        this.adStartTime = adStartTime;
    }

    /**
     * @return The adEndDate
     */
    public String getAdEndDate() {
        return adEndDate;
    }

    /**
     * @param adEndDate The ad_end_date
     */
    public void setAdEndDate(String adEndDate) {
        this.adEndDate = adEndDate;
    }

    /**
     * @return The adEndTime
     */
    public String getAdEndTime() {
        return adEndTime;
    }

    /**
     * @param adEndTime The ad_end_time
     */
    public void setAdEndTime(String adEndTime) {
        this.adEndTime = adEndTime;
    }

    /**
     * @return The shopId
     */
    public Integer getShopId() {
        return shopId;
    }

    /**
     * @param shopId The shop_id
     */
    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }

    /**
     * @return The shopName
     */
    public String getShopName() {
        return shopName;
    }

    /**
     * @param shopName The shop_name
     */
    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    /**
     * @return The shopUri
     */
    public String getShopUri() {
        return shopUri;
    }

    /**
     * @param shopUri The shop_uri
     */
    public void setShopUri(String shopUri) {
        this.shopUri = shopUri;
    }

    /**
     * @return The statAvgClick
     */
    public String getStatAvgClick() {
        return statAvgClick;
    }

    /**
     * @param statAvgClick The stat_avg_click
     */
    public void setStatAvgClick(String statAvgClick) {
        this.statAvgClick = statAvgClick;
    }

    /**
     * @return The statTotalSpent
     */
    public String getStatTotalSpent() {
        return statTotalSpent;
    }

    /**
     * @param statTotalSpent The stat_total_spent
     */
    public void setStatTotalSpent(String statTotalSpent) {
        this.statTotalSpent = statTotalSpent;
    }

    /**
     * @return The statTotalImpression
     */
    public String getStatTotalImpression() {
        return statTotalImpression;
    }

    /**
     * @param statTotalImpression The stat_total_impression
     */
    public void setStatTotalImpression(String statTotalImpression) {
        this.statTotalImpression = statTotalImpression;
    }

    /**
     * @return The statTotalClick
     */
    public String getStatTotalClick() {
        return statTotalClick;
    }

    /**
     * @param statTotalClick The stat_total_click
     */
    public void setStatTotalClick(String statTotalClick) {
        this.statTotalClick = statTotalClick;
    }

    /**
     * @return The statTotalCtr
     */
    public String getStatTotalCtr() {
        return statTotalCtr;
    }

    /**
     * @param statTotalCtr The stat_total_ctr
     */
    public void setStatTotalCtr(String statTotalCtr) {
        this.statTotalCtr = statTotalCtr;
    }

    /**
     * @return The statTotalConversion
     */
    public String getStatTotalConversion() {
        return statTotalConversion;
    }

    /**
     * @param statTotalConversion The stat_total_conversion
     */
    public void setStatTotalConversion(String statTotalConversion) {
        this.statTotalConversion = statTotalConversion;
    }

    /**
     * @return The labelEdit
     */
    public String getLabelEdit() {
        return labelEdit;
    }

    /**
     * @param labelEdit The label_edit
     */
    public void setLabelEdit(String labelEdit) {
        this.labelEdit = labelEdit;
    }

    /**
     * @return The labelPerClick
     */
    public String getLabelPerClick() {
        return labelPerClick;
    }

    /**
     * @param labelPerClick The label_per_click
     */
    public void setLabelPerClick(String labelPerClick) {
        this.labelPerClick = labelPerClick;
    }

    /**
     * @return The labelOf
     */
    public String getLabelOf() {
        return labelOf;
    }

    /**
     * @param labelOf The label_of
     */
    public void setLabelOf(String labelOf) {
        this.labelOf = labelOf;
    }

    /**
     *
     * @return
     * The itemId
     */
    public Integer getItemId() {
        return itemId;
    }

    /**
     *
     * @param itemId
     * The item_id
     */
    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    /**
     *
     * @return
     * The adModerated
     */
    public Integer getAdModerated() {
        return adModerated;
    }

    /**
     *
     * @param adModerated
     * The ad_moderated
     */
    public void setAdModerated(Integer adModerated) {
        this.adModerated = adModerated;
    }

    /**
     *
     * @return
     * The adModeratedReason
     */
    public String getAdModeratedReason() {
        return adModeratedReason;
    }

    /**
     *
     * @param adModeratedReason
     * The ad_moderated_reason
     */
    public void setAdModeratedReason(String adModeratedReason) {
        this.adModeratedReason = adModeratedReason;
    }

    /**
     *
     * @return
     * The productName
     */
    public String getProductName() {
        return productName;
    }

    /**
     *
     * @param productName
     * The product_name
     */
    public void setProductName(String productName) {
        this.productName = productName;
    }

    /**
     *
     * @return
     * The productUri
     */
    public String getProductUri() {
        return productUri;
    }

    /**
     *
     * @param productUri
     * The product_uri
     */
    public void setProductUri(String productUri) {
        this.productUri = productUri;
    }


}
