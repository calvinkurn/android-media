package com.tokopedia.topads.dashboard.data.model.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zulfikarrahman on 2/28/17.
 */

public class CreateGroupRequest {

    @SerializedName("group_name")
    @Expose
    private String groupName;
    @SerializedName("shop_id")
    @Expose
    private String shopId;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("price_bid")
    @Expose
    private Integer priceBid;
    @SerializedName("price_daily")
    @Expose
    private Integer priceDaily;
    @SerializedName("group_budget")
    @Expose
    private String groupBudget;
    @SerializedName("group_schedule")
    @Expose
    private String groupSchedule;
    @SerializedName("group_start_date")
    @Expose
    private String groupStartDate;
    @SerializedName("group_start_time")
    @Expose
    private String groupStartTime;
    @SerializedName("group_end_date")
    @Expose
    private String groupEndDate;
    @SerializedName("group_end_time")
    @Expose
    private String groupEndTime;
    @SerializedName("sticker_id")
    @Expose
    private String stickerId;
    @SerializedName("group_total")
    @Expose
    private String groupTotal;
    @SerializedName("source")
    @Expose
    private String source;
    @SerializedName("ads")
    @Expose
    private List<AdCreateGroupRequest> ads = new ArrayList();

    @SerializedName("suggested_bid_value")
    @Expose
    private long suggestionBidValue;
    @SerializedName("is_suggestion_bid_button")
    @Expose
    private String suggestionBidButton;

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getPriceBid() {
        return priceBid;
    }

    public void setPriceBid(Integer priceBid) {
        this.priceBid = priceBid;
    }

    public Integer getPriceDaily() {
        return priceDaily;
    }

    public void setPriceDaily(Integer priceDaily) {
        this.priceDaily = priceDaily;
    }

    public String getGroupBudget() {
        return groupBudget;
    }

    public void setGroupBudget(String groupBudget) {
        this.groupBudget = groupBudget;
    }

    public String getGroupSchedule() {
        return groupSchedule;
    }

    public void setGroupSchedule(String groupSchedule) {
        this.groupSchedule = groupSchedule;
    }

    public String getGroupStartDate() {
        return groupStartDate;
    }

    public void setGroupStartDate(String groupStartDate) {
        this.groupStartDate = groupStartDate;
    }

    public String getGroupStartTime() {
        return groupStartTime;
    }

    public void setGroupStartTime(String groupStartTime) {
        this.groupStartTime = groupStartTime;
    }

    public String getGroupEndDate() {
        return groupEndDate;
    }

    public void setGroupEndDate(String groupEndDate) {
        this.groupEndDate = groupEndDate;
    }

    public String getGroupEndTime() {
        return groupEndTime;
    }

    public void setGroupEndTime(String groupEndTime) {
        this.groupEndTime = groupEndTime;
    }

    public String getStickerId() {
        return stickerId;
    }

    public void setStickerId(String stickerId) {
        this.stickerId = stickerId;
    }

    public String getGroupTotal() {
        return groupTotal;
    }

    public void setGroupTotal(String groupTotal) {
        this.groupTotal = groupTotal;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public List<AdCreateGroupRequest> getAds() {
        return ads;
    }

    public void setAds(List<AdCreateGroupRequest> ads) {
        this.ads = ads;
    }

    public long getSuggestionBidValue() {
        return suggestionBidValue;
    }

    public void setSuggestionBidValue(long suggestionBidValue) {
        this.suggestionBidValue = suggestionBidValue;
    }

    public String getSuggestionBidButton() {
        return suggestionBidButton;
    }

    public void setSuggestionBidButton(String suggestionBidButton) {
        this.suggestionBidButton = suggestionBidButton;
    }
}
